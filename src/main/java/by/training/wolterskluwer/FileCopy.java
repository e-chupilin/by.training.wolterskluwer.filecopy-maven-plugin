package by.training.wolterskluwer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Mojo(name = "filecopy", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)

public class FileCopy extends AbstractMojo {
	public static final String INPUT_DIR = "${project.build.directory}/input";
	public static final String INPUT_FILE = "my.properties";
	public static final String OUTPUT_DIR = "${project.build.directory}/output";
	public static final String OUTPUT_FILE = "my_new.properties";
	public static final CopyOption COPY_OPTION = StandardCopyOption.REPLACE_EXISTING;

	@Parameter(defaultValue = INPUT_DIR)
	private String inputDirectory;

	@Parameter(defaultValue = INPUT_FILE)
	private String inputFile;

	@Parameter(defaultValue = OUTPUT_DIR)
	private String outputDirectory;

	@Parameter(defaultValue = OUTPUT_FILE)
	private String outputFile;

	@Parameter(property = "project.name")
	private String projectName;

	public void execute() throws MojoExecutionException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info(" Start plugin for copy file. Project: \n\t" + projectName);
		getLog().info("------------------------------------------------------------------------");

		try {
			Path pathInputFile = Paths.get(inputDirectory, inputFile);
			Path pathOutputFile = Paths.get(outputDirectory, outputFile);
			Path pathOutputDirectory = Paths.get(outputDirectory);

			getLog().info("Try to copy: " + "\n\t From: " + pathInputFile.toString() + "\n\t To: "
					+ pathOutputFile.toString() + "\n\t Option: " + COPY_OPTION.toString().toLowerCase());

			if (!Files.exists(pathInputFile))
				throw new FileNotFoundException(pathInputFile.toString());

			if (Files.exists(pathOutputDirectory)) {
				getLog().info("Output directory already created.");
			} else {
				Files.createDirectories(pathOutputDirectory);
				getLog().info("Create directory: " + pathOutputDirectory);
			}
			Files.copy(pathInputFile, pathOutputFile, COPY_OPTION);
			getLog().info("File was copied.");
		} catch (FileNotFoundException e) {
			getLog().error("Copy error. File not found: " + e.getMessage());
		} catch (IOException e) {
			getLog().error("File copy error: " + e.getMessage());
		}
	}
}
