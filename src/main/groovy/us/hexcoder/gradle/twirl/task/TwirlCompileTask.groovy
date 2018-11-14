package us.hexcoder.gradle.twirl.task

import org.gradle.api.tasks.TaskAction

/**
 * @author 67726e
 */
class TwirlCompileTask extends AbstractCompileTask {
	TwirlCompileTask() {
		setDescription("Compiles all Twirl templates in the ${project.twirl.sourceDirectory}")
		inputs.dir project.twirl.sourceDirectory
		outputs.dir project.twirl.targetDirectory
	}

	@TaskAction
	void compileTwirl() {
		logger.debug("Compile source Twirl templates")
		compile((String)project.twirl.sourceDirectory, (String)project.twirl.targetDirectory)
	}
}
