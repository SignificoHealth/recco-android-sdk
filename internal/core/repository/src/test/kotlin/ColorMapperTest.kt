import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.repository.mapper.rearrangeHexColorAlphaToStart
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test

class ColorMapperTest {

    @Test
    fun `When valid input, result is the rearranged color`() {
        val hexColor = "#AABBCCDD"
        val result = hexColor.rearrangeHexColorAlphaToStart()
        assertThat(result).isEqualTo("#DDAABBCC")
    }

    @Test
    fun `When invalid input format, it throws IllegalArgumentException`() {
        val hexColor = "AABBCCDD" // Missing '#' at the start
        val exception = assertThrows(IllegalStateException::class.java) {
            hexColor.rearrangeHexColorAlphaToStart()
        }
        assertThat(exception.message)
            .contains("(AABBCCDD) color does not have a valid format: #RRGGBBAA")
    }

    @Test
    fun `When invalid input length, it throws IllegalArgumentException`() {
        val hexColor = "#AABBCCD" // Length is not 9
        val exception = assertThrows(IllegalStateException::class.java) {
            hexColor.rearrangeHexColorAlphaToStart()
        }
        assertThat(exception.message)
            .contains("(#AABBCCD) color does not have a valid format: #RRGGBBAA")
    }
}
