package com.fincance.dataprocess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Balint Csikos on 5/26/2017.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class EntrypointTest {
    @Test
    public void calcChange() throws Exception {
        assertEquals("0", Entrypoint.calcChange(-1, 5.0));
        assertEquals("50.0", Entrypoint.calcChange(10.0, 20.0));
    }

}
