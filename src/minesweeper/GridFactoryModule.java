package minesweeper;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import minesweeper.model.IGridFactory;

public class GridFactoryModule extends AbstractModule {
	private Provider<IGridFactory> provider;


	@Override
	protected void configure() {
		bind(IGridFactory.class).toProvider(provider);
	}

	public void setProvider(Provider<IGridFactory> provider) {
		this.provider = provider;
	}
}
