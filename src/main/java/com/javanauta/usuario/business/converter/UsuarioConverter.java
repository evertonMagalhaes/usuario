package com.javanauta.usuario.business.converter;

import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList; // Importado para usar o ArrayList
import java.util.List;

@Component
public class UsuarioConverter {

    // ==========================================================
    // MÉTODOS DE DTO -> PARA ENTIDADE
    // ==========================================================

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEndereco()))
                .telefones(paraListaTelefones(usuarioDTO.getTelefone()))
                .build();
    }

    // Usando loop 'for'
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS){
        if (enderecoDTOS == null) return null;
        List<Endereco> enderecos = new ArrayList<>();
        for (EnderecoDTO enderecoDTO : enderecoDTOS) {
            enderecos.add(paraEndereco(enderecoDTO));
        }
        return enderecos;
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .build();
    }

    // Usando loop 'for'
    public List<Telefone> paraListaTelefones(List<TelefoneDTO> telefoneDTOS){
        if (telefoneDTOS == null) return null;
        List<Telefone> telefones = new ArrayList<>();
        for (TelefoneDTO telefoneDTO : telefoneDTOS) {
            telefones.add(paraTelefone(telefoneDTO));
        }
        return telefones;
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    // ==========================================================
    // MÉTODOS DE ENTIDADE -> PARA DTO
    // ==========================================================

    public UsuarioDTO paraUsuarioDTO(Usuario usuario){
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                // ⚠️ Segurança: Nunca retorne a senha criptografada.
                .senha(null)
                .endereco(paraListaEnderecoDTO(usuario.getEnderecos()))
                .telefone(paraListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    // Usando loop 'for'
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecos){
        if (enderecos == null) return null;
        List<EnderecoDTO> enderecoDTOS = new ArrayList<>();
        for (Endereco endereco : enderecos) {
            enderecoDTOS.add(paraEnderecoDTO(endereco));
        }
        return enderecoDTOS;
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco){
        return EnderecoDTO.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .build();
    }

    // Usando loop 'for'
    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefones){
        if (telefones == null) return null;
        List<TelefoneDTO> telefoneDTOS = new ArrayList<>();
        for (Telefone telefone : telefones) {
            telefoneDTOS.add(paraTelefoneDTO(telefone));
        }
        return telefoneDTOS;
    }

    // Converte Entidade -> DTO (Corrigido para devolver TelefoneDTO)
    public TelefoneDTO paraTelefoneDTO(Telefone telefone){
        return TelefoneDTO.builder()
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }
}