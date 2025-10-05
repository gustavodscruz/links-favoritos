#!/bin/bash
set -e  # para parar caso dê erro

# ============================
# VARIÁVEIS
# ============================
RESOURCE_GROUP_NAME="rg-links-favoritos"
WEBAPP_NAME="links-favoritos-api"
APP_SERVICE_PLAN="links-favoritos-plan"
LOCATION="brazilsouth"
RUNTIME="JAVA:21-java21"

RG_DB_NAME="rg-sql-links-favoritos"
DB_USERNAME="linksfavoritos-admin"
DB_NAME="linksfavoritos-db"
DB_PASSWORD="Links@Fav0rit0s!#"
SERVER_NAME="sql-server-linksfavoritos"

GITHUB_REPO_NAME="gustavodscruz/links-favoritos"
BRANCH="main"
APP_INSIGHTS_NAME="ai-links-favoritos"

# ============================
# PROVIDERS E EXTENSÕES
# ============================
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.OperationalInsights
az provider register --namespace Microsoft.ServiceLinker
az provider register --namespace Microsoft.Sql

az extension add --name application-insights || true

# ============================
# GRUPOS DE RECURSOS
# ============================
az group create --name $RG_DB_NAME --location eastus2
az group create --name $RESOURCE_GROUP_NAME --location "$LOCATION"

# ============================
# BANCO DE DADOS SQL
# ============================
az sql server create \
  --name $SERVER_NAME \
  --resource-group $RG_DB_NAME \
  --location eastus2 \
  --admin-user $DB_USERNAME \
  --admin-password $DB_PASSWORD \
  --enable-public-network true

az sql db create \
  --resource-group $RG_DB_NAME \
  --server $SERVER_NAME \
  --name $DB_NAME \
  --service-objective Basic \
  --backup-storage-redundancy Local \
  --zone-redundant false

# Liberar firewall (apenas testes!)
az sql server firewall-rule create \
  --resource-group $RG_DB_NAME \
  --server $SERVER_NAME \
  --name liberaGeral \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255

# ============================
# APPLICATION INSIGHTS
# ============================
az monitor app-insights component create \
  --app $APP_INSIGHTS_NAME \
  --location "$LOCATION" \
  --resource-group $RESOURCE_GROUP_NAME \
  --application-type web

CONNECTION_STRING=$(az monitor app-insights component show \
  --app $APP_INSIGHTS_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --query connectionString \
  --output tsv)

# ============================
# APP SERVICE PLAN + WEBAPP
# ============================
az appservice plan create \
  --name $APP_SERVICE_PLAN \
  --resource-group $RESOURCE_GROUP_NAME \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux

az webapp create \
  --name $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --plan $APP_SERVICE_PLAN \
  --runtime "$RUNTIME"

# Habilitar autenticação SCM
az resource update \
  --resource-group $RESOURCE_GROUP_NAME \
  --namespace Microsoft.Web \
  --resource-type basicPublishingCredentialsPolicies \
  --name scm \
  --parent sites/$WEBAPP_NAME \
  --set properties.allow=true

# ============================
# CONFIGURAR VARIÁVEIS DO APP
# ============================
SPRING_DATASOURCE_URL="jdbc:sqlserver://$SERVER_NAME.database.windows.net:1433;database=$DB_NAME"

az webapp config appsettings set \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --settings \
    APPLICATIONINSIGHTS_CONNECTION_STRING="$CONNECTION_STRING" \
    ApplicationInsightsAgent_EXTENSION_VERSION="~3" \
    XDT_MicrosoftApplicationInsights_Mode="Recommended" \
    XDT_MicrosoftApplicationInsights_PreemptSdk="1" \
    SPRING_DATASOURCE_USERNAME=$DB_USERNAME \
    SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \
    SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL

# Reiniciar o Web App
az webapp restart --name $WEBAPP_NAME --resource-group $RESOURCE_GROUP_NAME

# Conectar App ao Application Insights
az monitor app-insights component connect-webapp \
    --app $APP_INSIGHTS_NAME \
    --web-app $WEBAPP_NAME \
    --resource-group $RESOURCE_GROUP_NAME

# ============================
# DEPLOY VIA GITHUB ACTIONS
# ============================
az webapp deployment github-actions add \
  --name $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --repo $GITHUB_REPO_NAME \
  --branch $BRANCH \
  --login-with-github

echo "✅ Deploy do links-favoritos configurado com sucesso!"
