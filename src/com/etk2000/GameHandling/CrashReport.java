package com.etk2000.GameHandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.GUIFont;

public class CrashReport {
	/** Holds the keys and values of all crash report sections. */
	private final ArrayList<String> crashReportJunk = new ArrayList<String>();
	/*
	 * 0: Game Version
	 * 1: OS
	 * 2: Java Version
	 * 3: Java VM Version
	 * 4: Memory
	 * 5: JVM Flags
	 */
	/** the file where the report is saved **/
	private File crashReportFile;
	private final StackTraceElement[] stackTrace;
	private final String description, type;
	private final Throwable cause;

	public CrashReport(String description, Throwable cause) {
		this.description = description;
		this.cause = cause;
		this.type = cause.getClass().toString();
		this.stackTrace = cause.getStackTrace();
		// this.populateEnvironment();
	}

	/**
	 * Populates this crash report with initial information about the running server and operating
	 * system / java
	 * environment
	 */
	/*private void populateEnvironment() {
		crashReportJunk.add("" + GameBase.version);// 0
		crashReportJunk.add(System.getProperty("os.name") + ' ' + System.getProperty("os.arch"));// 1
		crashReportJunk.add(System.getProperty("java.specification.version"));// 2
		crashReportJunk.add(System.getProperty("java.vm.version"));// 3
		crashReportJunk.add(new CallableMemoryInfo(this));
		crashReportJunk.add(new CallableJVMFlags(this));
	}*/

	@SuppressWarnings("unchecked")
	public void render() {
		UnicodeFont font = null;

		cause.printStackTrace();

		Display.setTitle("I crashed...");
		String comment = getWittyComment();

		try {
			font = new UnicodeFont(GamePackHandler.fontLoc, 18, false, false);
			font.addAsciiGlyphs();
			// font.addGlyphs(400, 600);
			// font.addGlyphs(0, 65525);// all Unicode Glyphs
			font.getEffects().add(new ColorEffect());
			font.loadGlyphs();
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			GUIFont.center(font, DisplayHandler.width / 2, DisplayHandler.height / 2, comment);
			font.drawString(0, 0, type + ":");
			font.drawString(0, 20, cause.getMessage());
			for (int i = 0; i < stackTrace.length; i++)
				font.drawString(0, 100 + 25 * i, stackTrace[i].toString(), org.newdawn.slick.Color.red);

			Display.update();
			Display.sync(60);
		}
	};

	/**
	 * Returns the description of the Crash Report.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the Throwable object that is the cause for the crash.
	 */
	public Throwable getCrashCause() {
		return cause;
	}

	/**
	 * Gets the stack trace of the Throwable that caused this crash, or if that fails, the
	 * cause .toString().
	 */
	public String getCauseStackTraceOrString() {
		StringWriter stringwriter = null;
		PrintWriter printwriter = null;
		String s = this.cause.toString();

		try {
			stringwriter = new StringWriter();
			printwriter = new PrintWriter(stringwriter);
			this.cause.printStackTrace(printwriter);
			s = stringwriter.toString();
		} finally {
			try {
				if (stringwriter != null) {
					stringwriter.close();
				}

				if (printwriter != null) {
					printwriter.close();
				}
			}
			catch (IOException e) {
			}
		}

		return s;
	}

	/**
	 * Gets the complete report with headers, stack trace, and different sections as a string.
	 */
	public String getCompleteReport() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("---- SpaceGame Crash Report ----\n");
		stringbuilder.append("// ");
		stringbuilder.append(getWittyComment());
		stringbuilder.append("\n\n");
		stringbuilder.append("Time: ");
		stringbuilder.append((new SimpleDateFormat()).format(new Date()));
		stringbuilder.append("\n");
		stringbuilder.append("Description: ");
		stringbuilder.append(this.description);
		stringbuilder.append("\n\n");
		stringbuilder.append(this.getCauseStackTraceOrString());
		stringbuilder
				.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

		for (int i = 0; i < 87; ++i) {
			stringbuilder.append("-");
		}

		stringbuilder.append("\n\n");
		return stringbuilder.toString();
	}

	/**
	 * Gets the file this crash report is saved into.
	 */
	public File getFile() {
		return this.crashReportFile;
	}

	/**
	 * Saves the complete crash report to the given File.
	 */
	public boolean saveToFile(File file) {
		if (this.crashReportFile != null) {
			return false;
		}
		else {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}

			try {
				FileWriter filewriter = new FileWriter(file);
				filewriter.write(this.getCompleteReport());
				filewriter.close();
				this.crashReportFile = file;
				return true;
			}
			catch (Throwable throwable) {
				System.out.println("Could not save crash report to " + file + "\nReason: " + throwable.getMessage());
				return false;
			}
		}
	}

	/**
	 * Gets a random witty comment for inclusion in this CrashReport (I copied this from Minecraft)
	 */
	private static String getWittyComment() {
		String[] astring = new String[] { "I see the light!", "Oops...", "Well, this is embarrassing..." };

		try {
			return astring[(int) (System.nanoTime() % (long) astring.length)];
		}
		catch (Throwable throwable) {
			return "Witty comment unavailable :(";
		}
	}

	/**
	 * Creates a crash report for the exception
	 */
	public static CrashReport makeCrashReport(Throwable throwable, String description) {
		CrashReport crashreport;
		crashreport = new CrashReport(description, throwable);
		return crashreport;
	}
}