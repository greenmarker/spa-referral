import junit.framework.TestCase;
import org.junit.Test;
import pl.hackerspace.nfz.SpaReferralReader;

/**
 * Created by Kamil on 2015-03-21.
 */
public class SpaReferralReaderTest extends TestCase {

    private static final String TEST_REFERRAL_NUMBER = "07-13-65044-6";

    @Test
    public void testReader(){
        // given
        SpaReferralReader reader = new SpaReferralReader();

        // when
        int number = reader.getNumberInQueue(TEST_REFERRAL_NUMBER);

        // then
        assertEquals("Wrong number.", 28589, number);
    }
}
