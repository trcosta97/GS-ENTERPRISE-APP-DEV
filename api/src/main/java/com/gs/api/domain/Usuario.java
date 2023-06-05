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
    @Column(name = "nome_usuario", nullable= false)
    private String nome;
    @Column(name = "email_usuario", nullable= false)
    private String email;
    @Column(name = "senha_usuario", nullable= false)
    private String senha;
    @Column(name = "cnpj_usuario", nullable= false)
    private String cnpj;
    @JoinColumn(name = "id_endereco", nullable= false)
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private EnderecoUsuario enderecoUsuario;
    @Column(name = "data_cadastro", nullable= false)
    @Temporal(TemporalType.DATE)
    private LocalDate dataCadastro;
    @Column(name = "usuario_ativo", columnDefinition = "BIT(1) DEFAULT 1", nullable= false)
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
