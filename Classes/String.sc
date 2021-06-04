/*
 * FILENAME: String
 *
 * DESCRIPTION:
 *         String extension methods.
 *
 * NOTES:
 * - First install quark https://github.com/shimpe/scstringext due to the usage of the String.replaceRegex extension method.
 * - Quarks.install("https://github.com/shimpe/scstringext");
 * AUTHOR: Marinus Klaassen (2021Q2)
 * - Dependend on scstringext
 */

+String {

	xmlMinify {
		^this
		.replaceRegex("\>(\s+)\<", "")
		.replace(Char.nl, "")
	}
}
