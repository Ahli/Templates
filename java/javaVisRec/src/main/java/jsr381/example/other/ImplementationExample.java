package jsr381.example.other;

import javax.visrec.spi.ServiceProvider;

/**
 * Example which shows the name and version of the implementation of JSR 381
 *
 * @author Kevin Berendsen
 */
public class ImplementationExample {
	
	public static void main(final String[] args) {
		System.out.println("VisRec API (JSR 381) implementation: " +
				ServiceProvider.current().getImplementationService().toString());
	}
}
