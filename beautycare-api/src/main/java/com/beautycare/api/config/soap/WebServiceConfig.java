package com.beautycare.api.config.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource; // Para cargar el XSD
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs // Habilita las funcionalidades de Spring Web Services (SOAP)
@Configuration // Marca la clase como configuración de Spring
public class WebServiceConfig extends WsConfigurerAdapter {

    /**
     * Registra el servlet MessageDispatcherServlet de Spring WS.
     * Este servlet manejará todas las peticiones SOAP entrantes.
     * Lo mapeamos a la URL /soap-ws/*
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        // Habilita la detección automática de WSDL en la URL (ej. /soap-ws/clientes.wsdl)
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/soap-ws/*"); // Mapeo de URL
    }

    /**
     * Define cómo se generará el archivo WSDL (el contrato del servicio).
     * Usamos DefaultWsdl11Definition, que lo genera a partir de un esquema XSD.
     *
     * @param clienteSchema El Bean XsdSchema que definimos abajo.
     * @return La definición del WSDL.
     */
    @Bean(name = "clientes") // Nombre del WSDL (ej. /soap-ws/clientes.wsdl)
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema clienteSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ClientesPort"); // Nombre del PortType en el WSDL
        wsdl11Definition.setLocationUri("/soap-ws");     // URL base del servicio
        // Define el namespace objetivo (targetNamespace) - IMPORTANTE
        wsdl11Definition.setTargetNamespace("http://beautycare.com/api/ws/clientes");
        wsdl11Definition.setSchema(clienteSchema);       // Asocia el esquema XSD
        return wsdl11Definition;
    }

    /**
     * Carga nuestro esquema XSD (que definirá las peticiones y respuestas SOAP).
     * Lo buscaremos en 'src/main/resources/xsd/clientes.xsd'.
     *
     * @return El Bean XsdSchema.
     */
    @Bean
    public XsdSchema clienteSchema() {
        // Carga el archivo XSD desde el classpath
        return new SimpleXsdSchema(new ClassPathResource("xsd/clientes.xsd"));
    }
}