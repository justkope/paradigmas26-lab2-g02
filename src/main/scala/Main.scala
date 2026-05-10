// =====================================================================
// Ejercicio 6: Integración del sistema completo
// =====================================================================

object Main {
  def main(args: Array[String]): Unit = {

    // ------------------------------------------------------------------
    // Paso 1: Cargar diccionarios
    // ------------------------------------------------------------------
    // TODO (Ejercicio 2)
    val dictionary: List[NamedEntity] = Dictionary.loadAll()

    println(s"Diccionario cargado: ${dictionary.size} entidades.\n")

    // ------------------------------------------------------------------
    // Paso 2: Descargar posts
    // ------------------------------------------------------------------
    val subscriptions = FileIO.readSubscriptions()

    val allPosts: List[(String, List[String])] = subscriptions.map { url =>
      println(s"Descargando posts de: $url")
      val json   = FileIO.downloadFeed(url)
      val titles = FileIO.extractPostTitles(json)
      (url, titles)
    }

    // ------------------------------------------------------------------
    // Paso 3: Detectar entidades y mostrar resultados por post
    // ------------------------------------------------------------------
    allPosts.foreach { case (url, titles) =>
      titles.foreach { titulo =>
        val entidades = Analyzer.detectEntities(titulo, dictionary)
        val count = Analyzer.countByType(entidades)
        //val starpoint = Analyzer.starPoint(entidades)
        println(Formatters.formatNERResult(titulo, entidades))
        println(Formatters.formatEntityStats(count))
        //println(Formatters.formatStarPoint(starpoint)) lo comento pq me rompe todo
      }
    }
    // ------------------------------------------------------------------
    // Paso 4: Estadísticas globales
    // ------------------------------------------------------------------
    // TODO (Ejercicios 5 y 6):
    //   1. Recolectar TODAS las entidades detectadas en todos los posts
    //   2. Contar por tipo
    //   3. Mostrar el resumen

  }
}
