# Global Solution 2023/1 - ENTERPRISE APPLICATION DEVELOPMENT - FoodHelp

## Sobre o projeto:

A fome é um problema global que afeta milhões de pessoas em todo o mundo. 
Segundo a Organização das Nações Unidas para a Alimentação e Agricultura 
(FAO), cerca de 828 milhões de pessoas foram afetadas pela fome em 2021, 
enquanto 2,3 bilhões de pessoas sofrem de insegurança alimentar moderada 
ou grave em 2021.

A escassez de alimentos é um fator que contribui para a fome, sendo que 
muitas regiões sofrem com a falta de acesso a alimentos básicos, como arroz, 
trigo e milho. Esse problema é agravado por questões como mudanças 
climáticas, conflitos armados, desigualdade social e econômica, e desastres 
naturais. 

Por isso, é fundamental que haja um esforço global no combate à fome e na 
garantia de acesso a alimentos para todas as pessoas. Medidas como a 
promoção da agricultura sustentável, o investimento em sistemas de 
armazenamento e distribuição de alimentos, a eliminação do desperdício 
alimentar e a redução das desigualdades sociais e econômicas são essenciais 
para enfrentar esse desafio. 

Com isso, o projeto “FoodHelp”, um aplicativo inovador que ofereça uma 
solução eficiente para o problema do desperdício alimentar ao conectar 
empresas, como restaurantes, padarias, supermercados e outros tipos de 
comércio, a centros de caridade e organizações sem fins lucrativos. O objetivo 
é garantir que os alimentos não sejam desperdiçados, mas sim direcionados 
para aqueles que mais precisam. 

O aplicativo permite que empresas de diferentes setores se cadastrem 
facilmente, fornecendo informações, como nome, localização, horário de 
funcionamento e tipo de negócio. Isso inclui estabelecimentos que lidam com 
alimentos. Após o cadastro, as empresas podem fotografar os alimentos que 
sobraram ou estão próximos da data de validade. Com um recurso de 
inteligência artificial, pela detecção de imagens, o aplicativo é capaz de 
identificar os alimentos. As fotos dos alimentos e suas informações são 
exibidas no aplicativo, onde as organizações de caridade e centros de apoio 
podem visualizá-las. As empresas têm acesso a uma lista atualizada de
alimentos disponíveis para doação. 

Com uma plataforma de comunicação entre as empresas e as instituições de 
caridade, isso permite um planejamento sobre as negociações sobre o local de 
retirada, horário, entre outros detalhes logísticos que poderão ser tratados. A 
comunicação direta facilita a colaboração, evitando desperdício devido a 
problemas de logística.

## Instalação e execução da API

A API foi desenvolvida usando  Java 17 e com o framework Spring + Gradle e banco de dados relacional Oracle SQL.
O primeiro passo é buildar a aplicação. Abra um prompt de comando na pasta raiz do projeto e execute o comando:
```console
admin:~$ gradle build
admin
```
Se o seu Java e Gradle estiverem configurados corretamente, o projeto deve buildar sem problemas e estar pronto para a inicialização.
O próximo passo é executar o método Main presente na classe ApiApplication que se encontra no caminho *src/main/java/com/gs/api*.

O projeto contém um swagger, que é um console que roda junto com o programa e serve para testes. Ele oferece uma interface gráfica html com todos os endpoints e seus respectivos *json* formatados já com o os campos necesários para cada endpoint e o tipo de dado que ele precisa.

A configuração do banco de dados é feita pelo arquivo application.properties. Lá é possível que mudar o login, senha e endereço do seu banco de dados Oracle.  

## Mapemanto JPA

### Classe Alimento

```console
@Entity(name = "alimentos")
@Table(name = "tb_alimentos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_alimento")
    private Long id;
    @Column(name = "data_doacao")
    @CreatedDate
    @Temporal(TemporalType.DATE)
    private LocalDate dataDoacao;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "alimentos_tags", joinColumns = @JoinColumn(name = "alimentos_id_alimento"))
    @Column(name = "tag")
    private List<String> tags;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurante_doador_id", nullable = false)
    private Restaurante restauranteDoador;
    @Column(name = "alimento_ativo", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean ativo = true;

    public Alimento(AlimentoDTO dadosAlimento) {
        this.tags = dadosAlimento.tags();
        this.restauranteDoador = new Restaurante(dadosAlimento.restauranteDoadorId());
    }

    public void desativarAlimento(){
        this.ativo = false;
    }

    @PrePersist
    protected void prePersist() {
        dataDoacao = LocalDate.now();
    }
}
```

### Classe Restaurante
```console
@Entity(name = "restaurante")
@Table(name = "tb_restaurantes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_restaurante")
    private Long id;
    @Column(name = "nome_restaurante")
    private String nome;
    @Column(name = "email_restaurante", unique = true)
    private String email;
    @Column(name = "senha_restaurante")
    private String senha;
    @Column(name = "cnpj_restaurante")
    private String cnpj;
    @JoinColumn(name = "id_endereco")
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private EnderecoRestaurante enderecoRestaurante;
//    @OneToMany(mappedBy = "restauranteDoador", cascade = CascadeType.ALL)
//    private List<Alimento> alimentos;
    @Column(name = "data_cadastro", updatable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dataCadastro;
    @Column(name = "restaurante_ativo", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean ativo = true;

    public Restaurante(RestauranteDTO dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.senha = dados.senha();
        this.enderecoRestaurante = new EnderecoRestaurante(dados.endereco());
        this.cnpj = dados.cnpj();
    }


    public Restaurante(Long id) {
        this.id = id;
    }

    public void desativarRestaurante(){
        this.ativo = false;
    }

    @PrePersist
    protected void prePersist() {
        dataCadastro = LocalDate.now();
    }

}
```

### Classe Usuario

```console
@Entity(name = "usuario" )
@Table(name="tb_usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
    private Long id;
    @Column(name = "nome_usuario")
    private String nome;
    @Column(name = "email_usuario", unique = true)
    private String email;
    @Column(name = "senha_usuario")
    private String senha;
    @Column(name = "cnpj_usuario")
    private String cnpj;
    @JoinColumn(name = "id_endereco")
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private EnderecoUsuario enderecoUsuario;
    @Column(name = "data_cadastro")
    @Temporal(TemporalType.DATE)
    private LocalDate dataCadastro;
    @Column(name = "usuario_ativo", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean ativo = true;

    public Usuario(UsuarioDTO dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.senha = dados.senha();
        this.enderecoUsuario = new EnderecoUsuario (dados.endereco());
        this.cnpj = dados.cnpj();
    }

    public void desativarUsuario(){
        this.ativo = false;
    }

    @PrePersist
    protected void prePersist() {
        dataCadastro = LocalDate.now();
    }
}


```

### Classe Endereço Restaurante

```console
@Entity(name = "endereco_restaurante")
@Table(name="tb_endereco_restaurantes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoRestaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_endereco")
    private Long id;
    @Column(name = "cep", nullable = false)
    private String cep;
    @Column(name = "logradouro", nullable = false)
    private String logradouro;
    @Column(name="numero")
    private String numero;
    @Column(name = "uf", nullable = false)
    @Enumerated(EnumType.STRING)
    private Uf uf;
    @Column(name = "complemento")
    private String complemento;
    @JoinColumn(name = "id_restaurante")
    @OneToOne(mappedBy = "enderecoRestaurante")
    @JsonBackReference
    private Restaurante restaurante;
    @Column(name = "endereco_ativo", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean ativo;

    public void desativarEndereco(){
        this.ativo = false;
    }

    public EnderecoRestaurante(EnderecoRestauranteDTO dados) {
        this.cep = dados.cep();
        this.uf = dados.uf();
        this.complemento = dados.complemento();
        this.logradouro = dados.logradouro();
        this.numero = dados.numero();
    }
```

### Classe Endereço Usuário

```console
@Entity(name = "endereco_usuario")
@Table(name="tb_endereco_usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_endereco")
    private Long id;
    @Column(name = "cep", nullable = false)
    private String cep;
    @Column(name = "logradouro", nullable = false)
    private String logradouro;
    @Column(name="numero")
    private String numero;
    @Column(name = "uf", nullable = false)
    @Enumerated(EnumType.STRING)
    private Uf uf;
    @Column(name = "complemento")
    private String complemento;
    @JoinColumn(name = "id_usuario")
    @OneToOne(mappedBy = "enderecoUsuario")
    @JsonBackReference
    private Usuario usuario;
    @Column(name = "endereco_ativo", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean ativo = true;



    public EnderecoUsuario(EnderecoUsuarioDTO dados) {
        this.cep = dados.cep();
        this.uf = dados.uf();
        this.complemento = dados.complemento();
        this.logradouro = dados.logradouro();
        this.numero = dados.numero();
    }

    public void desativarEndereco(){
        this.ativo = false;
    }


}
```

## Métodos JPQL 

### Busca de restaurante por nome:

```console
public List<Restaurante> buscarRestaurantePorNome(String nome) {

    String jpql = "SELECT r FROM Restaurante r WHERE r.nome = :nome";
    TypedQuery<Restaurante> query = em.createQuery(jpql, Restaurante.class);
    query.setParameter("nome", nome);

    return query.getResultList();
}
```
### Busca de alimento por tag:
```console
public List<Alimento> buscarAlimentoPorTag(String tag) {

    String jpql = "SELECT a FROM Alimento a WHERE :tag MEMBER OF a.tags";
    TypedQuery<Alimento> query = em.createQuery(jpql, Alimento.class);
    query.setParameter("tag", tag);

    return query.getResultList();
}

```

### Busca de usuário por UF:

```console
public List<Usuario> buscarUsuariosPorUF(Uf uf) {

    String jpql = "SELECT u FROM Usuario u WHERE u.enderecoUsuario.uf = :uf";
    TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
    query.setParameter("uf", uf);

    return query.getResultList();
}

```


## Lista de ENDPOINTS:  

### POST /usuarios
Endpoint responsável pelo cadastro de novos usuário. Contém validação de email (precisa conter "@") e de CNPJ (14 digitos).  
*Json*:  
  ```console
  {  
  "nome": "string",  
  "email": "string",  
  "senha": "string",  
  "cnpj": "string",  
  "endereco": {  
    "cep": "string",  
    "logradouro": "string",  
    "numero": "string",  
    "uf": "AC",  
    "complemento": "string"  
  }
}
 ```

### POST /usuarios/login  
Endpoint responsável pelo login de usuários. Não funciona pede um *json*, mas sim 2 parametros: email e senha, e busca no banco por usuários cadastrados com o mesmo login e senha informados.  


### POST /restaurantes/  
Endpoint responsável pelo cadastro de novos usuário. Contém validação de email (precisa conter "@") e de CNPJ (14 digitos).  
*Json*:  
  ```console
  {  
  "nome": "string",  
  "email": "string",  
  "senha": "string",  
  "cnpj": "string",  
  "endereco": {  
    "cep": "string",  
    "logradouro": "string",  
    "numero": "string",  
    "uf": "AC",  
    "complemento": "string"  
  }  
}  
  ```
### POST /restaurantes/login  
Endpoint responsável pelo login de restaurantes. Não funciona pede um *json*, mas sim 2 parametros: email e senha, e busca no banco por restaurantes cadastrados com o mesmo login e senha informados.  


### GET /restaurantes  
Endpoint que retorna lista com todos os restaurantes listados por ordem de cadastro. Não pede *json*, pede dois parametros: email (*String*) e senha (*String*).  

### GET /restaurantes/busca  
Endpoint que retorna restaurante a partir do nome. Não pede *json*, pede o parametro nome (*String*)

### POST /alimentos  
Endpoint responsável pelo cadastro de alimentos. Recebe o id do restaurante que faz a doação e uma lista de strings com os alimentos.  
*Json*:  
  ```console
  {  
  "tags": [  
    "string"  
  ],  
  "restauranteDoadorId": 0  
}
```

### GET /alimentos  
Endpoint que retorna lista com todos os alimentos listados por ordem de cadastro. Não pede *json*.  


### DELETE /alimentos  
Endpoint responsável pela exclusão lógica de alimentos. Usado assim que um alimentos é escolhido por um usuário. Muda o atributo *ativo* do alimento de *true* pra *false*. Não necessita de *json*, recebe um paramentro *Id* (*Long*).  

##Vídeo Explicativo

[[https://youtu.be/r59V-HrQtII](https://youtu.be/2SMXKWx_bWg)](https://youtu.be/r59V-HrQtII)



