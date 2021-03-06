package music.list.util
import java.util.regex.Matcher
import java.util.regex.Pattern

object MobileNumberValidator {
    private val Mobile_PATTERN = "^[0-9]{10,13}\$"
    private var pattern: Pattern = Pattern.compile(Mobile_PATTERN)
    private var matcher: Matcher? = null

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    fun validate(hex: String): Boolean {
        matcher = pattern.matcher(hex)
        return matcher!!.matches()
    }

    fun trimEnd(s: String): String {
        if (s.isEmpty()) return s
        var i = s.length
        while (i > 0 && Character.isWhitespace(s[i - 1])) i--
        return if (i == s.length) s else s.substring(0, i)
    }
}