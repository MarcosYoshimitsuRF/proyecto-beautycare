package com.beautycare.api.endpoint;

// Importar las clases generadas por JAXB (ajusta el paquete si es diferente)
import com.beautycare.api.ws.clientes.ConsultarEstadoClienteRequest;
import com.beautycare.api.ws.clientes.ConsultarEstadoClienteResponse;
// Importar Repositorio y Modelo
import com.beautycare.api.model.Cliente;
import com.beautycare.api.repository.ClienteRepository;
// Importar anotaciones de Spring WS
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Optional;

@Endpoint // Marca esta clase como un Endpoint SOAP
public class ClienteEndpoint {

    // Namespace definido en el XSD y WebServiceConfig
    private static final String NAMESPACE_URI = "http://beautycare.com/api/ws/clientes";

    @Autowired
    private ClienteRepository clienteRepository; // Inyecta el repositorio

    /**
     * Maneja las peticiones SOAP para ConsultarEstadoClienteRequest.
     *
     * @param request El objeto de petición, mapeado desde el XML SOAP entrante.
     * @return El objeto de respuesta, que será mapeado al XML SOAP saliente.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarEstadoClienteRequest") // Vincula este método a la petición
    @ResponsePayload // Indica que el valor de retorno es el payload de la respuesta
    public ConsultarEstadoClienteResponse consultarEstadoCliente(@RequestPayload ConsultarEstadoClienteRequest request) {

        ConsultarEstadoClienteResponse response = new ConsultarEstadoClienteResponse();
        String clienteIdString = request.getClienteId(); // Obtiene el ID del cliente de la petición

        // Lógica de negocio simulada (busca por ID, podrías buscar por email, DNI, etc.)
        String estado;
        try {
            Long clienteId = Long.parseLong(clienteIdString);
            Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);

            if (clienteOpt.isPresent()) {
                // Simulación simple de estado basado en si tiene email o no
                if (clienteOpt.get().getEmail() != null && !clienteOpt.get().getEmail().isEmpty()) {
                    estado = "ACTIVO";
                } else {
                    estado = "INCOMPLETO";
                }
            } else {
                estado = "NO_ENCONTRADO";
            }
        } catch (NumberFormatException e) {
            estado = "ID_INVALIDO"; // Si el ID no es numérico
        }

        // Establece el estado en la respuesta
        response.setEstadoCliente(estado);
        return response; // Devuelve la respuesta
    }
}