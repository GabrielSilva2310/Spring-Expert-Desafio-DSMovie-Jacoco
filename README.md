# Java Spring Expert Desafio DSMovie Jacoco 


# Sobre o projeto
Este projeto é o desafio do capítulo sobre Cobertura de testes com Jacoco do Curso Java Spring Expert da [DevSuperior](https://devsuperior.com.br "Site da DevSuperior").
O desafio consiste em  implementar todos os testes unitários de service para o projeto DSMovie.


## Especificações e Modelo Conceitual
Este é um projeto de filmes e avaliações de filmes. A visualização dos dados dos filmes é pública (não necessita login), porém as alterações de filmes (inserir, atualizar, deletar) são permitidas apenas para usuários ADMIN. As avaliações de filmes podem ser registradas por qualquer usuário logado CLIENT ou ADMIN. A entidade Score armazena uma nota de 0 a 5 (score) que cada usuário deu a cada filme. Sempre que um usuário registra uma nota, o sistema calcula a média das notas de todos usuários, e armazena essa nota média (score) na entidade Movie, juntamente com a contagem de votos (count)
![Modelo Conceitual](https://github.com/GabrielSilva2310/Assets/blob/main/Images%20Java%20Spring%20Expert/Modelo%20Conceitual%20DSMOVIE%20JACOCO.png)

Testes que deveram ser implementados:

MovieServiceTests:

-findAllShouldReturnPagedMovieDTO

-findByIdShouldReturnMovieDTOWhenIdExists

-findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist

-insertShouldReturnMovieDTO

-updateShouldReturnMovieDTOWhenIdExists

-updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist

-deleteShouldDoNothingWhenIdExists

-deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist

-deleteShouldThrowDatabaseExceptionWhenDependentId

ScoreServiceTests:

-saveScoreShouldReturnMovieDTO

-saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId

UserServiceTests:

-authenticatedShouldReturnUserEntityWhenUserExists

-authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists

-loadUserByUsernameShouldReturnUserDetailsWhenUserExists

-loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists



# Tecnologias utilizadas
- Java 17
- Spring Boot 3
- Maven
- JPA / Hibernate
- H2 Database
- JUnit 5
- Jacoco
- Postman

# Postman Collection
  Para testar a API, você pode usar a coleção do Postman disponível no link abaixo:
  
  [Download](https://github.com/GabrielSilva2310/Assets/blob/main/Postman%20Collections%20and%20Enviroments/Spring%20Expert/DSMovieJACOCO/Desafio%20DSMovie%20Jacoco.postman_collection%20(1).json)

# Como executar o projeto

Pré-requisitos: Java 17

```bash
# clonar repositório
git clone https://github.com/GabrielSilva2310/Spring-Expert-Desafio-DSMovie-Jacoco.git
```
Importar Projeto para uma IDE de sua escolha , e executar o Maven install , após acessar a pasta do projeto e ir em target\jacoco-report\index.html para acessar o relatório da cobertura de testes.

Use o Postman para fazer as requisições e verificar se os resultados correspondem aos testes.

# Competências avaliadas no desafio
- Testes unitários em projeto Spring Boot com Java
- Implementação de testes unitários com JUnit e Mockito
- Cobertura de código com Jacoco

# Autor

Gabriel Da Silva 

www.linkedin.com/in/gabriel-da-silva-457039193
