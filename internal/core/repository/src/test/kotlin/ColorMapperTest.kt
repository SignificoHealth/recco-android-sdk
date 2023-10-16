import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.repository.mapper.rearrangeHexColorAlphaToStart
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test

class ColorMapperTest {

    @Test
    fun `When rearrangeHexColorAlphaToStart with valid input, then result should be the rearranged color`() {
        val hexColor = "#AABBCCDD"
        val result = hexColor.rearrangeHexColorAlphaToStart()
        assertThat(result).isEqualTo("#DDAABBCC")
    }

    @Test
    fun `When rearrangeHexColorAlphaToStart with invalid input format, then it should throw IllegalArgumentException with a message containing #RRGGBBAA valid format`() {
        val hexColor = "AABBCCDD" // Missing '#' at the start
        val exception = assertThrows(IllegalStateException::class.java) {
            hexColor.rearrangeHexColorAlphaToStart()
        }
        assertThat(exception.message)
            .contains("(AABBCCDD) color does not have a valid format: #RRGGBBAA")
    }

    @Test
    fun `When rearrangeHexColorAlphaToStart with invalid input length, then it should throw IllegalArgumentException with a message containing #RRGGBBAA valid format`() {
        val hexColor = "#AABBCCD" // Length is not 9
        val exception = assertThrows(IllegalStateException::class.java) {
            hexColor.rearrangeHexColorAlphaToStart()
        }
        assertThat(exception.message)
            .contains("(#AABBCCD) color does not have a valid format: #RRGGBBAA")
    }
}
