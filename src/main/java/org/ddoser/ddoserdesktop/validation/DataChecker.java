package org.ddoser.ddoserdesktop.validation;

import java.net.UnknownHostException;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Утилитарный класс для проверок
 */
public class DataChecker {

  /**
   * Конструктор
   */
  private DataChecker() {
  }

  /**
   * Проверить URL на валидность
   *
   * @param url адрес
   * @return признак правильности и доступности ресурса
   */
  public static ErrorObject isUrlValid(String url) {
    if (ObjectUtils.isEmpty(url)) {
      return ErrorObject.invalid("Передан пустой адрес");
    }

    try {
      new RestTemplate().getForObject(url, String.class);
      return ErrorObject.valid();
    } catch (ResourceAccessException e) {
      if (e.getCause() instanceof UnknownHostException) {
        return ErrorObject.invalid("Хост не найден");
      }
      return ErrorObject.invalid("Ошибка обращения к ресурсу");
    } catch (HttpClientErrorException e) {
      if (HttpStatus.FORBIDDEN.equals(e.getStatusCode())) {
        return ErrorObject.invalid("Доступ к ресурсу запрещен");
      } else if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode())) {
        return ErrorObject.invalid("Требуется авторизация");
      }
      return ErrorObject.invalid(
        "Клиентская ошибка с кодом: %s".formatted(e.getStatusCode().value()));
    } catch (HttpServerErrorException e) {
      return ErrorObject.invalid(
        "Ошибка на стороне сервера с кодом: %s".formatted(e.getStatusCode().value()));
    } catch (Exception e) {
      return ErrorObject.invalid("Невалидный URL");
    }
  }
}
