/**
 * Created by: Tomek Spędzia
 * Date: 5/3/2016
 * Email: tomek.milosz.spedzia@gmail.com
 */
package jacare.geocodingexample;

import android.location.Address;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import jacare.geocodingexample.model.LocHelper;

@RunWith(AndroidJUnit4.class)
public class LocHelperTest extends TestCase {
    protected CountDownLatch signal;
    protected LocHelper locHelper;
    protected boolean addressExistenceProof;

    @Override
    @Before
    public void setUp() throws Exception{
        signal = new CountDownLatch(1);
        locHelper = new LocHelper(InstrumentationRegistry.getContext());
    }

    @Test
    public void testConvertLatLngToAddressToTest() throws Exception {
        LatLng latLng = new LatLng(50.091721, 19.984128);
        locHelper.convertLatLngToAddress(latLng).subscribe(adresses -> finishAsyncTest(adresses));
        signal.await();
        assertTrue(addressExistenceProof);
    }

    protected void finishAsyncTest(List<Address> adresses){
        addressExistenceProof = adresses != null && adresses.size() > 0;
        signal.countDown();
    }
}