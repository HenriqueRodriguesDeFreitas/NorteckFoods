package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestEnderecoDTO;
import com.br.norteck.dtos.request.RequestFornecedorDTO;
import com.br.norteck.dtos.response.ResponseSupplierDTO;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.*;
import com.br.norteck.repository.*;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private BairroRepository bairroRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;

    public ResponseSupplierDTO save(RequestFornecedorDTO request) {
     fornecedorRepository.findByCnpj(request.cnpj()).ifPresent(s ->{
         throw new EntityNotFoundException(String.format(MessageError.SUPPLIER_EXISTS_WITH_CNPJ, request.cnpj()));
     });
     fornecedorRepository.findByNomeFantasiaIgnoreCase(request.fantasyName()).ifPresent(s -> {
         throw new RuntimeException(String.format("Já exite Supplier com o nome fantasia: %s", request.fantasyName()));
     });
     fornecedorRepository.findByRazaoSocialIgnoreCase(request.corporateReason()).ifPresent(s -> {
         throw new RuntimeException(String.format("Já exite Supplier com razão social: %s", request.corporateReason()));
     });
     fornecedorRepository.findByInscricaoEstadual(request.stateRegistration()).ifPresent(s -> {
         throw new RuntimeException(String.format("Já exite Supplier com a inscrição estadual: %s", request.corporateReason()));
     });

     Cidade cidade = cidadeRepository.findByNomeIgnoreCase(request.address().city())
             .orElseThrow(()-> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "City", request.address().city())));

     Estado estado = estadoRepository.findByNomeIgnoreCase(request.address().state())
             .orElseThrow(()-> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "State", request.address().state())));

     Bairro bairro = bairroRepository.findByNomeIgnoreCase(request.address().neighborhood())
             .orElseThrow(()-> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME,"Neighborhood", request.address().neighborhood())));



     Fornecedor newFornecedor = new Fornecedor();
     newFornecedor.setRazaoSocial(request.corporateReason());
     newFornecedor.setNomeFantasia(request.fantasyName());
     newFornecedor.setInscricaoEstadual(request.stateRegistration());
     newFornecedor.setCnpj(request.cnpj());


     List<Fornecedor> fornecedors = new ArrayList<>();
     fornecedors.add(newFornecedor);

     Endereco endereco = new Endereco();
     endereco.setRua(request.address().street());
     endereco.setCep(request.address().cep());
     endereco.setNumeroEndereco(request.address().numberAddress());
     endereco.setSupplier(fornecedors);
     newFornecedor.setAddress(endereco);


     if(!bairro.getCity().getNome().equalsIgnoreCase(cidade.getNome())){
         throw new RuntimeException(String.format("O bairro: %s não pertence a cidade: %s.", bairro.getNome(), cidade.getNome()));
     }
     if(!cidade.getState().getNome().equalsIgnoreCase(estado.getNome())){
         throw new RuntimeException(String.format("A cidade: %s não pertence ao estado: %s.", cidade.getNome(), estado.getNome()));
     }

     endereco.setNeighborhood(bairro);
     endereco.setCity(cidade);

     enderecoRepository.save(endereco);

     return convertObjectToDto(fornecedorRepository.save(newFornecedor));
    }

    public List<ResponseSupplierDTO> findAll() {
        List<Fornecedor> fornecedors = fornecedorRepository.findAll();
        return fornecedors.stream().map(this::convertObjectToDto)
                .collect(Collectors.toList());
    }

    public ResponseSupplierDTO findById(Integer id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Fornecedor", id)));
        return convertObjectToDto(fornecedor);
    }

    public List<ResponseSupplierDTO> findByNameFantasy(String nameFantasy) {
        List<Fornecedor> fornecedors = fornecedorRepository.findByNomeFantasiaContainingIgnoreCase(nameFantasy);
        return fornecedors.stream().map(this::convertObjectToDto)
                .collect(Collectors.toList());
    }

    public List<ResponseSupplierDTO> findByCorporateReason(String corporteReason) {
        List<Fornecedor> fornecedors = fornecedorRepository.findByRazaoSocialContainingIgnoreCase(corporteReason);
        return fornecedors.stream().map(this::convertObjectToDto)
                .collect(Collectors.toList());
    }

    public List<ResponseSupplierDTO> findByStateRegistration(String stateRegistration) {
        List<Fornecedor> fornecedors = fornecedorRepository.findByInscricaoEstadualContaining(stateRegistration);
        return fornecedors.stream().map(this::convertObjectToDto)
                .collect(Collectors.toList());
    }

    public ResponseSupplierDTO findByCnpj(Long cnpj) {
        Fornecedor fornecedor = fornecedorRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Fornecedor com o CNPJ: %014d não encontrado.", cnpj)));
        return convertObjectToDto(fornecedor);
    }

    @Transactional
    public ResponseSupplierDTO update(Integer id, RequestFornecedorDTO request) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Supplier", id)));

        Endereco endereco = enderecoRepository.findById(fornecedor.getAddress().getId())
                .orElseThrow(() -> new RuntimeException("Endereço da empresa não encontrado"));


        System.out.println("Antes de pesquiar o bairro");

        Bairro bairro = bairroRepository.findByNomeIgnoreCase(request.address().neighborhood())
                .orElseThrow(()-> new RuntimeException("Bairro não encontrado"));
        Cidade cidade = cidadeRepository.findByNomeIgnoreCase(request.address().city())
                .orElseThrow(()-> new RuntimeException("Cidade não encontrada"));
        Estado estado = estadoRepository.findByNomeIgnoreCase(request.address().state())
                .orElseThrow(()-> new RuntimeException("Estado não encontrado"));

        System.out.println("Antes do if");
        // Verificações de integridade relacional

        if(!bairro.getCity().getNome().equalsIgnoreCase(cidade.getNome())){
            throw new RuntimeException(String.format("O bairro: %s não pertence a cidade: %s.", bairro.getNome(), cidade.getNome()));
        }
        if(!cidade.getState().getNome().equalsIgnoreCase(estado.getNome())){
            throw new RuntimeException(String.format("A cidade: %s não pertence ao estado: %s.", cidade.getNome(), estado.getNome()));
        }

        cidade.setState(estado);

        // Atualizando dados do endereço
        endereco.setRua(request.address().street());
        endereco.setCep(request.address().cep());
        endereco.setNumeroEndereco(request.address().numberAddress());
        endereco.setNeighborhood(bairro);
        endereco.setCity(cidade);

        // Atualizando dados do fornecedor
        fornecedor.setNomeFantasia(request.fantasyName());
        fornecedor.setRazaoSocial(request.corporateReason());
        fornecedor.setCnpj(request.cnpj());
        fornecedor.setInscricaoEstadual(request.stateRegistration());
        fornecedor.setAddress(endereco);

        // Salvar alterações em cascata
        enderecoRepository.save(endereco);
        return convertObjectToDto(fornecedorRepository.save(fornecedor));
    }

    public void deleteById(Integer id){
        fornecedorRepository.deleteById(id);
    }

    private ResponseSupplierDTO convertObjectToDto(Fornecedor fornecedor) {
        RequestEnderecoDTO requestEnderecoDTO = new RequestEnderecoDTO(
                fornecedor.getAddress().getCity().getState().getNome(),
                fornecedor.getAddress().getCity().getNome(),
                fornecedor.getAddress().getNeighborhood().getNome(),
                fornecedor.getAddress().getRua(), fornecedor.getAddress().getNumeroEndereco(),
                fornecedor.getAddress().getCep()
        );

        RequestFornecedorDTO requestFornecedorDTO = new RequestFornecedorDTO(
                fornecedor.getNomeFantasia(), fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(), fornecedor.getInscricaoEstadual(), requestEnderecoDTO
        );
        return new ResponseSupplierDTO(fornecedor.getId(), requestFornecedorDTO);
    }

}
