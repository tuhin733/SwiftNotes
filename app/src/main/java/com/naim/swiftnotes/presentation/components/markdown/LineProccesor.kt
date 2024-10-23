package com.naim.swiftnotes.presentation.components.markdown

import androidx.compose.ui.graphics.Color

interface MarkdownLineProcessor {
    fun canProcessLine(line: String): Boolean
    fun processLine(line: String, builder: MarkdownBuilder)
}

class CodeBlockProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.startsWith("```")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val codeBlock = StringBuilder()
        var index = builder.lineIndex + 1
        var isEnded = false

        while (index < builder.lines.size) {
            val nextLine = builder.lines[index]
            if (nextLine == "```") {
                builder.lineIndex = index
                isEnded = true
                break
            }
            codeBlock.appendLine(nextLine)
            index++
        }

        builder.add(CodeBlock(codeBlock.toString(), isEnded, line))
    }
}

class CheckboxProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.matches(Regex("^\\[[ xX]]( .*)?"))

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val checked = line.contains(Regex("^\\[[Xx]]"))
        val text = line.replace(Regex("^\\[[ xX]] ?"), "").trim()
        builder.add(CheckboxItem(text, checked, builder.lineIndex))
    }
}

class LabelProcessor : MarkdownLineProcessor {
    // Define colors for specific label keywords
    private val labelColors = mapOf(
        "Important" to Color(0xFFEF5350),
        "To-Do" to Color(0xFF4CAF50),
        "Reminders" to Color(0xFF03A9F4),
        "Idea" to Color(0xFFFFCA28),
    )

    override fun canProcessLine(line: String): Boolean {
        // Check if the line starts with '#'
        return line.startsWith("[LABEL]") // Modify this to recognize label syntax
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        // Remove '#' and trim whitespace from the label text
        val labelText = line.drop(7).trim()

        val customColor = Color(0xFF009688)

        // Determine color based on keywords
        val color =
            labelColors.entries.firstOrNull { labelText.contains(it.key, ignoreCase = true) }?.value
                ?: customColor // Default color if no keywords match

        // Add label to the builder
        builder.add(Label(labelText, color)) // Modify to add Label instead of Heading
    }
}


class HeadingProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.startsWith("#")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val level = line.takeWhile { it == '#' }.length
        val text = line.drop(level).trim()
        builder.add(Heading(level, text))
    }
}

class QuoteProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.trim().startsWith(">")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val level = line.takeWhile { it == '>' }.length
        val text = line.drop(level).trim()
        builder.add(Quote(level, text))
    }
}

class ListItemProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.startsWith("- ")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val text = line.removePrefix("- ").trim()
        builder.add(ListItem(text))
    }
}

class ImageInsertionProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.trim().startsWith("!(") && line.trim().endsWith(")")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val photoUri = line.substringAfter("!(", "").substringBefore(")")
        builder.add(ImageInsertion(photoUri))
    }
}

class MarkdownLinkProcessor : MarkdownLineProcessor {
    private val linkRegex = Regex("\\[(.+)]\\((https?://[\\w./?=#]+)\\)")

    override fun canProcessLine(line: String): Boolean {
        return linkRegex.containsMatchIn(line)
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val matchResult = linkRegex.find(line)
        if (matchResult != null) {
            val (text, url) = matchResult.destructured
            builder.add(MarkdownLink(text, url))
        }
    }
}

class MarkdownAudioProcessor : MarkdownLineProcessor {
    // Regex to detect ![audio](path)
    private val audioRegex = Regex("!\\[audio]\\((.+)\\)")

    override fun canProcessLine(line: String): Boolean {
        return audioRegex.containsMatchIn(line)
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val matchResult = audioRegex.find(line)
        if (matchResult != null) {
            val (audioPath) = matchResult.destructured
            builder.add(AudioElement(audioPath))
        }
    }
}


