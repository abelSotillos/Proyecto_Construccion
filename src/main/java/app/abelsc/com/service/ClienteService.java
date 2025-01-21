package app.abelsc.com.service;

import app.abelsc.com.domain.Cliente;
import app.abelsc.com.repository.ClienteRepository;
import app.abelsc.com.service.dto.ClienteDTO;
import app.abelsc.com.service.mapper.ClienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.Cliente}.
 */
@Service
@Transactional
public class ClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Save a cliente.
     *
     * @param clienteDTO the entity to save.
     * @return the persisted entity.
     */
    public ClienteDTO save(ClienteDTO clienteDTO) {
        LOG.debug("Request to save Cliente : {}", clienteDTO);
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    /**
     * Update a cliente.
     *
     * @param clienteDTO the entity to save.
     * @return the persisted entity.
     */
    public ClienteDTO update(ClienteDTO clienteDTO) {
        LOG.debug("Request to update Cliente : {}", clienteDTO);
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    /**
     * Partially update a cliente.
     *
     * @param clienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClienteDTO> partialUpdate(ClienteDTO clienteDTO) {
        LOG.debug("Request to partially update Cliente : {}", clienteDTO);

        return clienteRepository
            .findById(clienteDTO.getId())
            .map(existingCliente -> {
                clienteMapper.partialUpdate(existingCliente, clienteDTO);

                return existingCliente;
            })
            .map(clienteRepository::save)
            .map(clienteMapper::toDto);
    }

    /**
     * Get all the clientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClienteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Clientes");
        return clienteRepository.findAll(pageable).map(clienteMapper::toDto);
    }

    /**
     * Get one cliente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> findOne(Long id) {
        LOG.debug("Request to get Cliente : {}", id);
        return clienteRepository.findById(id).map(clienteMapper::toDto);
    }

    /**
     * Delete the cliente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }
}
