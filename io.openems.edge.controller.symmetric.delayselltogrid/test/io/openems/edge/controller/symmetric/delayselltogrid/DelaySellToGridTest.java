package io.openems.edge.controller.symmetric.delayselltogrid;

import org.junit.Test;

import io.openems.common.types.ChannelAddress;
import io.openems.edge.common.test.AbstractComponentConfig;
import io.openems.edge.common.test.AbstractComponentTest.TestCase;
import io.openems.edge.common.test.DummyComponentManager;
import io.openems.edge.common.test.DummyConfigurationAdmin;
import io.openems.edge.controller.test.ControllerTest;
import io.openems.edge.ess.test.DummyManagedSymmetricEss;
import io.openems.edge.ess.test.DummyPower;
import io.openems.edge.meter.test.DummySymmetricMeter;

public class DelaySellToGridTest {

	@SuppressWarnings("all")
	public static class MyConfig extends AbstractComponentConfig implements Config {
		private final String ess_id;
		private final String meter_id;
		private final int delaySellToGridPower;

		public MyConfig(String id, String ess_id, String meter_id, int delaySellToGridPower) {
			super(Config.class, id);
			this.ess_id = ess_id;
			this.meter_id = meter_id;
			this.delaySellToGridPower = delaySellToGridPower;
		}

		@Override
		public String ess_id() {
			return this.ess_id;
		}

		@Override
		public String meter_id() {
			return this.meter_id;
		}

		@Override
		public int delaySellToGridPower() {
			return this.delaySellToGridPower;
		}
	}

	ChannelAddress essActivePower = new ChannelAddress("ess0", "ActivePower");
	ChannelAddress essSetPower = new ChannelAddress("ess0", "SetActivePowerEquals");
	ChannelAddress meter1ActivePower = new ChannelAddress("meter1", "ActivePower");

	@Test
	public void test() throws Exception {

		MyConfig myconfig = new MyConfig("ctrl1", "ess0", "meter1", 500_000);

		new ControllerTest(new DelaySellToGrid()) //
				.addReference("cm", new DummyConfigurationAdmin()) //
				.addReference("componentManager", new DummyComponentManager()) //
				.addReference("ess", new DummyManagedSymmetricEss("ess0", new DummyPower(30000))) //
				.addReference("meter", new DummySymmetricMeter("meter1"))//
				.activate(myconfig) //
				.next( //
						new TestCase() //
								.input(essActivePower, 0) //
								.input(meter1ActivePower, 490_000) //
								.output(essSetPower, 10000)) //
		;
	}
}
