package com.jkc.dictionary.swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import com.jkc.dictionary.echoenum.EchoEnumService
import com.jkc.dictionary.service.DictionaryService
import io.swagger.v3.oas.models.ExternalDocumentation

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses = Set(classOf[DictionaryService], EchoEnumService.getClass)
  override val host = "127.0.0.1:8080"
  override val info = Info(version = "1.0")
  override val externalDocs = Some(new ExternalDocumentation().description("Core Docs").url("http://acme.com/docs"))
  //override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}