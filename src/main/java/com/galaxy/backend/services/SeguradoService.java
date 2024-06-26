package com.galaxy.backend.services;

import com.galaxy.backend.dtos.PessoaFisicaDTO;
import com.galaxy.backend.dtos.PessoaJuridicaDTO;
import com.galaxy.backend.dtos.SeguradoDTO;
import com.galaxy.backend.dtos.SeguradoPageDTO;
import com.galaxy.backend.dtos.mapper.SeguradoMapper;
import com.galaxy.backend.models.Address;
import com.galaxy.backend.models.Corretor;
import com.galaxy.backend.models.Segurado;
import com.galaxy.backend.repositories.AddressRepository;
import com.galaxy.backend.repositories.CorretorRepository;
import com.galaxy.backend.repositories.SeguradoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Validated
public class SeguradoService {

    private final SeguradoRepository seguradoRepository;
    private final AddressRepository addressRepository;
    private final CorretorRepository corretorRepository;

    public SeguradoService(SeguradoRepository seguradoRepository, AddressRepository addressRepository, CorretorRepository corretorRepository) {
        this.addressRepository = addressRepository;
        this.seguradoRepository = seguradoRepository;
        this.corretorRepository = corretorRepository;
    }

    public PessoaFisicaDTO savePF(PessoaFisicaDTO data) {
        return SeguradoMapper.INSTANCE.toDTO(seguradoRepository.save(SeguradoMapper.INSTANCE.toEntity(data)));
    }

    public PessoaJuridicaDTO savePJ(PessoaJuridicaDTO data) {
        return SeguradoMapper.INSTANCE.toDTO(seguradoRepository.save(SeguradoMapper.INSTANCE.toEntity(data)));
    }

    public SeguradoDTO findById(@NotNull @Positive Long id) {
        return seguradoRepository.findById(id).map(SeguradoMapper.INSTANCE::toDTO).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<SeguradoDTO> findAll() {
        return seguradoRepository.findAll().stream().map(SeguradoMapper.INSTANCE::toDTO).toList();
    }

    public List<?> findSeguradoByTipo(String tipo) {
        return seguradoRepository.findSeguradoByTipoIgnoreCase(tipo).stream().map(SeguradoMapper.INSTANCE::toDTO).toList();
    }
    private SeguradoPageDTO listSegurados(String name, int pageNumber, int pageSize) {
        Page<Segurado> page = (name != null) ? seguradoRepository.findSeguradoByNameContainingIgnoreCase(name, PageRequest.of(pageNumber, pageSize)) : seguradoRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<SeguradoDTO> segurados = page.getContent().stream().map(SeguradoMapper.INSTANCE::toDTO).toList();
        long pfCount = seguradoRepository.countByTipoIgnoreCase("pf");
        long pjCount = seguradoRepository.countByTipoIgnoreCase("pj");
        return new SeguradoPageDTO(segurados, page.getTotalPages(), page.getTotalElements(), pfCount, pjCount);
    }
    public SeguradoPageDTO listAll(int pageNumber, int pageSize) {
        return listSegurados(null, pageNumber, pageSize);
    }

    public SeguradoPageDTO searchByName(String name, int pageNumber, int pageSize) {
        return listSegurados(name, pageNumber, pageSize);
    }

    public SeguradoDTO update(@NotNull @Positive Long id, @Valid @NotNull SeguradoDTO data) {
        return seguradoRepository.findById(id).map(segurado -> {
            segurado.setName(data.name());
            segurado.setDocument(data.document());
            segurado.setAddress(data.address());

            return SeguradoMapper.INSTANCE.toDTO(seguradoRepository.save(segurado));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public SeguradoDTO updateAddress(@NotNull @Positive Long id, @Valid @NotNull Address data) {
        return seguradoRepository.findById(id).map(segurado -> {
            var address = segurado.getAddress();
            if (address == null) {
                segurado.setAddress(data);
                return SeguradoMapper.INSTANCE.toDTO(seguradoRepository.save(segurado));
            }
            var addressFound = addressRepository.findById(address.getId()).map(result -> {
                result.setCep(data.getCep());
                result.setStreet(data.getStreet());
                result.setNumber(data.getNumber());
                result.setComplement(data.getComplement());
                result.setNeighborhood(data.getNeighborhood());
                result.setCity(data.getCity());
                result.setState(data.getState());
                result.setCountry(data.getCountry());
                return addressRepository.save(result);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            segurado.setAddress(addressFound);
            return SeguradoMapper.INSTANCE.toDTO(seguradoRepository.save(segurado));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public SeguradoDTO updateCorretor(@NotNull @Positive Long id, @Valid @NotNull Corretor data) {
        return seguradoRepository.findById(id).map(segurado -> {
            if (segurado.getCorretor() == null) {
                segurado.setCorretor(data);
                return SeguradoMapper.INSTANCE.toDTO(seguradoRepository.save(segurado));
            }
            var corretorFound = corretorRepository.findById(data.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            segurado.setCorretor(corretorFound);
            return SeguradoMapper.INSTANCE.toDTO(seguradoRepository.save(segurado));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public boolean delete(@NotNull @Positive Long id) {
        if (seguradoRepository.existsById(id)) {
            seguradoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
