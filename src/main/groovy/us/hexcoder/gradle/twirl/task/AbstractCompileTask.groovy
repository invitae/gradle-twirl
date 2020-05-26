package us.hexcoder.gradle.twirl.task


import scala.collection.immutable.Vector$
import scala.collection.immutable.Vector
import scala.collection.mutable.Builder

import java.nio.charset.Charset
import scala.io.Codec

import org.gradle.api.DefaultTask
import play.twirl.api.HtmlFormat
import play.twirl.api.JavaScriptFormat
import play.twirl.api.TxtFormat
import play.twirl.api.XmlFormat
import play.twirl.compiler.TwirlCompiler

/**
 * @author 67726e
 */
abstract class AbstractCompileTask extends DefaultTask {
	protected static final Map<String, String> FORMATTERS

	static {
		FORMATTERS = new HashMap<>()
		FORMATTERS.put("html", HtmlFormat.class.getCanonicalName())
		FORMATTERS.put("xml", XmlFormat.class.getCanonicalName())
		FORMATTERS.put("txt", TxtFormat.class.getCanonicalName())
		FORMATTERS.put("js", JavaScriptFormat.class.getCanonicalName())
	}

	def compile(String sourceDirectory, String targetDirectory) {
		File source = new File(getProject().projectDir, sourceDirectory)
		File target = new File(getProject().projectDir, targetDirectory)
		Builder<String, Vector<String>> impBuilder = new Vector$().newBuilder()
		for (String imp:  project.twirl.imports){
			impBuilder.$plus$eq(imp)
		}
		Codec codec = new Codec(Charset.forName((String)project.twirl.charset))

		final List<String> templates = findTemplates(source)

		for (String templatePath : templates) {
			final File templateFile = new File(templatePath)
			final String formatter = FORMATTERS.get(extensionOf(templateFile))
			logger.debug("Compiling Twirl template: " + templatePath)
			TwirlCompiler.compile(templateFile, source, target, formatter, impBuilder.result(), new Vector$().empty(), codec, false)
		}
	}

	private static List<String> findTemplates(File sourceDirectory) {
		return new FileNameFinder().getFileNames(sourceDirectory.absolutePath, '**/*.scala.*')
	}

	private static String extensionOf(File file) {
		String[] parts = file.getName().split("\\.", -1)
		return parts[parts.length - 1]
	}
}
