# Links Favoritos

Adicione seus links favoritos e que são os seus mais usados! Guarde-os por categorias!

## Rodando o arquivo `deploy.sh`

1. Após executar o script na Azure, adicione as variáveis do env no seu Github. Vá em Settings > Secrets and Variables > Actions.

2. Adicione o seguinte código dentro do main-workflow.yaml!

```yaml
- name: Build with Maven
      run: mvn clean install
      #...
      #parte que voce deve adicionar está abaixo
      env: 
        SPRING_DATASOURCE_URL: ${{ secrets.SPRING_DATASOURCE_URL }}
        SPRING_DATASOURCE_USERNAME: ${{ secrets.SPRING_DATASOURCE_USERNAME }}
        SPRING_DATASOURCE_PASSWORD: ${{ secrets.SPRING_DATASOURCE_PASSWORD }}
```