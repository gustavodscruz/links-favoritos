# Links Favoritos - DimDim

![logo-dimdim](src/main/resources/static/images/dimdim-logo.png)

Adicione seus links favoritos e que são os seus mais usados! Guarde-os por categorias!

## Integrantes

| Nome | RM | Turma |
|------|----|-------|
| Gustavo Dias da Silva Cruz | 556448 | 2TSDPH |
| Júlia Medeiros Angelozi | 556364 | 2TDSPH |
| Felipe Ribeiro Tardochi da Silva | 555100 | 2TDSPH |

## Como rodar na nuvem

Temos um script `deploy.sh` que criará tudo que é necessário: plan service, web app, ai monitor, entre outros. Siga a seguinte ordem e execute os comandos no Cloudshell da Azure, com modo bash:

1. Pegue uma chave para Gif Api [aqui](https://developers.giphy.com/).

2. Exporte ela no terminal com: `export GIF_API_KEY="sua-chave-aqui"`

3. Dê permissão para o script com `chmod +x ./deploy.sh`

3. Após executar o script na Azure, adicione as variáveis do env no seu Github. Vá em Settings > Secrets and Variables > Actions.

Coloque os seguintes token/variaveis:

- SPRING_DATASOURCE_URL 
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- GIF_API_KEY

**IMPORTANTE**
### O token GIF_API_KEY está no entregável pdf, eu não quis expô-lo no código! 

Os tokens `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` estão no seu próprio script, já para `SPRING_DATASOURCE_URL` será necessário pegar dentro do portal da Azure, dentro do recurso de banco de dados sqlserver em `Cadeia de conexões` > `JDBC`. 

O `GIF_API_KEY` lembrando, é o token da api do [Giphy](https://developers.giphy.com/).

4. Adicione o seguinte código dentro do arquivo de workflow.yaml criado:

```yaml
- name: Build with Maven
      run: mvn clean install
      #...
      #parte que voce deve adicionar está abaixo
      env: 
        SPRING_DATASOURCE_URL: ${{ secrets.SPRING_DATASOURCE_URL }}
        SPRING_DATASOURCE_USERNAME: ${{ secrets.SPRING_DATASOURCE_USERNAME }}
        SPRING_DATASOURCE_PASSWORD: ${{ secrets.SPRING_DATASOURCE_PASSWORD }}
        GIF_API_KEY: ${{ secrets.GIF_API_KEY }}
```

5. Dê commit e push nas alterações. 

Se tudo for executado corretamente, meus parabéns, o projeto está no ar na Azure! Com recursos de monitoração, banco de dados sql server, web api e mais.

## Observações

**IMPORTANTE**
### O token GIF_API_KEY está no entregável pdf, eu não quis expô-lo no código! 

O script ddl das tabelas está na raiz do projeto com nome de `script_ddl.sql`. Ele é apenas as migrações postas em ordem em um arquivo.

