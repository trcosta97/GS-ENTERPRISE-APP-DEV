package com.gs.api.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;


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
    @Column(name = "email_usuario")
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
