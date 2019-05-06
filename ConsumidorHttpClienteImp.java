package mipaquete.spring.utilidades.servicio.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**<b>Descripci&oacute;n:</b><br>
* Esta clase implementa los diferentes funcionalidades necesarias para realizar peticiones http
* @author Oscar Mera - OM
* @since Fecha de creaci&oacute;n: 13/FEB/2019<br>
* Versi&oacute;n de JDK: 1.7
*/
public class ConsumidorHttpClienteImp implements ConsumidorHttpClienteInt {

  private String url;
  private MediaType tipoContenido;
  private Map<String, String> encabezados;
  private static RestTemplate rest;

  public ConsumidorHttpClienteImp(String url, MediaType tipoContenido,Map<String, String> encabezados) {
    super();
    this.url = url;
    this.tipoContenido = tipoContenido;
    this.encabezados = encabezados;
    obtenerInstanciaRestTemplate();
  }
  
  public ConsumidorHttpClienteImp(String url, MediaType tipoContenido) {
    super();
    this.url = url;
    this.tipoContenido = tipoContenido;
    obtenerInstanciaRestTemplate();
  }

  public static RestTemplate obtenerInstanciaRestTemplate() {
    if (rest == null) {
      rest = new RestTemplate();
    }
    return rest;
  }

 
  /**<br><b>Descripci&oacute;n:</b><br>
   * Metodo que se encarga de realizar peticiones http con el verbo POST
   * @param uri
   * @param datos
   * @param tipoRespuesta
   * @return
   * @throws Exception
   */
  public <T> T consumirPorPost(String uri, String datos,final Class<T> tipoRespuesta) throws Exception {

    T respuesta = null;
    try {
      List<MediaType> headerAccept = new ArrayList<MediaType>();
      headerAccept.add(this.tipoContenido);

      HttpHeaders requestEncabezado = new HttpHeaders();
      requestEncabezado.setContentType(this.tipoContenido);
      requestEncabezado.setAccept(headerAccept);
      if (this.encabezados != null && !this.encabezados.isEmpty()) {
        for (Entry<String, String> entry : this.encabezados.entrySet()) {
          requestEncabezado.add(entry.getKey(), entry.getValue());
        }
      }
      HttpEntity<Object> requestEntity =new HttpEntity<Object>(datos == null ? "" : datos, requestEncabezado);
      ResponseEntity<T> respuestaEntity = rest.exchange(this.url.concat(uri), HttpMethod.POST, requestEntity, tipoRespuesta);

      HttpStatus status = respuestaEntity.getStatusCode();
      if (status.equals(HttpStatus.OK) || status.equals(HttpStatus.CREATED)) {
        respuesta =  respuestaEntity.getBody();
      }

    } catch (Exception e) {
      getLog().error(e);
      throw e;
    }
    return respuesta;
  }
  
  
  /**<br><b>Descripci&oacute;n:</b><br>
   * Metodo que se encarga de realizar peticiones http con el verbo GET
   * @param uri
   * @param tipoRespuesta
   * @return
   * @throws Exception
   */
  public <T> T consumirPorGet(String uri,final Class<T> tipoRespuesta) throws Exception {

    T respuesta = null;
    try {
      List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
      acceptableMediaTypes.add(this.tipoContenido);

      HttpHeaders requestEncabezado = new HttpHeaders();
      requestEncabezado.setContentType(this.tipoContenido);
      requestEncabezado.setAccept(acceptableMediaTypes);
      if (this.encabezados != null && !this.encabezados.isEmpty()) {
        for (Entry<String, String> entry : this.encabezados.entrySet()) {
          requestEncabezado.add(entry.getKey(), entry.getValue());
        }
      }
      HttpEntity<Object> requestEntity =new HttpEntity<Object>("", requestEncabezado);
      ResponseEntity<T> respuestaEntity = rest.exchange(this.url.concat(uri), HttpMethod.GET, requestEntity, tipoRespuesta);

      HttpStatus status = respuestaEntity.getStatusCode();
      if (status.equals(HttpStatus.OK)) { 
        respuesta =  respuestaEntity.getBody();
      }

    } catch (Exception e) {
      getLog().error(e);
      throw e;
    }
    return respuesta;
  }

  private Log getLog() {
    return LogFactory.getLog("ConsumidorHttpClienteImp");
  }

}
