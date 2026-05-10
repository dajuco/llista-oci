package ui.app

import android.content.Context
import java.io.File

/**
 * Prepara l'emmagatzematge intern d'Android amb les dades JSON inicials.
 *
 * El repositori de l'aplicació treballa amb fitxers locals i, per tant, aquesta
 * classe copia els JSON empaquetats dins dels assets a la ruta que espera el
 * codi compartit.
 */
object AndroidJsonBootstrap {

    /**
     * Copia els JSON inicials als fitxers interns de l'aplicació.
     *
     * També redefineix `user.dir` perquè [repository.RepositorioJson] resolgui
     * correctament els camins relatius sense modificar la seva implementació.
     *
     * @param context context de l'aplicació Android.
     */
    fun inicialitzar(context: Context) {
        System.setProperty("user.dir", context.filesDir.absolutePath)

        val baseDir = File(context.filesDir, "src/main/kotlin/jsons")
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }

        copiarAsset(context, "jsons/categorias.json", File(baseDir, "categorias.json"))
        copiarAsset(context, "jsons/elementos.json", File(baseDir, "elementos.json"))
        copiarAsset(context, "jsons/usuarios.json", File(baseDir, "usuarios.json"))
    }

    private fun copiarAsset(context: Context, assetPath: String, desti: File) {
        if (desti.exists() && desti.length() > 0L) {
            return
        }

        context.assets.open(assetPath).use { input ->
            desti.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}

