// =====================================================================
// Ejercicio 6: Integración del sistema completo
// =====================================================================

object Main {
  def main(args: Array[String]): Unit = {

    // ------------------------------------------------------------------
    // Paso 1: Cargar diccionarios
    // ------------------------------------------------------------------



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
        val starpoint = Analyzer.starPoint(entidades)
        println(Formatters.formatNERResult(titulo, entidades))
        println(Formatters.formatEntityStats(count))
        println(Formatters.formatStarPoint(starpoint))
      }
    }

    val allEntities: List[NamedEntity] =
    allPosts.flatMap { case (_, titles) =>
      titles.flatMap { titulo =>
        Analyzer.detectEntities(titulo, dictionary)
      }
    }

    val globalCounts = Analyzer.countByType(allEntities)
    val globalStarPoint = Analyzer.starPoint(allEntities)

    println(Formatters.formatEntityStats(globalCounts))
    println(Formatters.formatStarPoint(globalStarPoint))


  }
}
