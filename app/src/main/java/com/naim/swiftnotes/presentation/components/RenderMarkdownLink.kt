import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.sp

@Composable
fun RenderMarkdownLink(
    displayText: String,
    url: String,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val uriHandler = LocalUriHandler.current

    Text(
       fontSize = 14.sp,
        text = buildAnnotatedString {
            val link = LinkAnnotation.Url(
                url = url,
                styles = TextLinkStyles(SpanStyle(color = color))
            ) {
                val linkUrl = (it as LinkAnnotation.Url).url
                // Log metrics or handle logic here
                uriHandler.openUri(linkUrl)
            }
            withLink(link) {
                append(displayText)
            }
        }
    )
}

