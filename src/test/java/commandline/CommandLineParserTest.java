package commandline;

import java.util.List;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

public class CommandLineParserTest {

	@Test
	public void testSimpleConfiguration() throws Exception {
		String[] args = new String[]{"-f","hello.txt","-v"};
		SimpleConfiguration simpleConfiguration = CommandLineParser.parse(SimpleConfiguration.class, args);
		String filename = simpleConfiguration.getFilename();
		boolean verbose = simpleConfiguration.getVerbose();
		assertEquals("hello.txt",filename);
		assertTrue(verbose);
	}
	
	@Test
	public void testMultipleArgsConfiguration() throws Exception {
		String[] args = new String[]{"--files","hello.txt","world.txt","bye.txt","--logfile","hello.log"};
		MultipleArgsConfiguration config = CommandLineParser.parse(MultipleArgsConfiguration.class, args);
		List<String> files = config.getFiles();
		String logfile = config.getLogfile();
		assertThat(files,hasItems("hello.txt","world.txt","bye.txt"));
		assertThat(logfile,is("hello.log"));
	}
	
	@Test
	public void testDelimiterConfiguran() throws Exception {
		String[] args = new String[]{"--exec","ls","-l","*.txt",";","--logfile","hello.log"};
		DelimiterConfiguration config = CommandLineParser.parse(DelimiterConfiguration.class, args);
		List<String> command = config.getCommand();
		String logfile = config.getLogfile();
		assertThat(command,hasItems("ls","-l","*.txt"));
		assertThat(logfile,is("hello.log"));
	}

	@Test
	public void testSubConfiguran() throws Exception {
		String[] args = new String[]{"--verbose","--album","--name","Caustic Grip","--artist","Front Line Assembly","--year","1990","--available","--logfile","hello.log"};
		SimpleSuperConfiguration config = CommandLineParser.parse(SimpleSuperConfiguration.class, args);
		AlbumConfiguration album = config.getAlbum();
		assertThat(config.getLogfile(),is("hello.log"));
		assertThat(config.getVerbose(),is(true));
		assertThat(album.getName(),is("Caustic Grip"));
		assertThat(album.getArtist(),is("Front Line Assembly"));
		assertThat(album.getYear(),is("1990"));
		assertThat(album.isAvailable(),is(true));
	}

	@Test
	public void testMultipleSubConfiguran() throws Exception {
		String[] args = new String[]{"--verbose",
									 "--album","--name","Caustic Grip","--artist","Front Line Assembly","--year","1990","--available",
									 "--album","--name","Scintilla","--artist","Stendeck","--year","2011","--available",
									 "--logfile","hello.log"};
		MultipleSubconfigsConfiguration config = CommandLineParser.parse(MultipleSubconfigsConfiguration.class, args);
		boolean verbose = config.getVerbose();
		List<AlbumConfiguration> albums = config.getAlbums();
		assertThat(albums.size(),is(2));
		AlbumConfiguration causticGrip = albums.get(0);
		AlbumConfiguration scintilla = albums.get(1);
		assertThat(causticGrip.getName(),is("Caustic Grip"));
		assertThat(causticGrip.getArtist(),is("Front Line Assembly"));
		assertThat(causticGrip.getYear(),is("1990"));
		assertThat(causticGrip.isAvailable(),is(true));
		assertThat(scintilla.getName(),is("Scintilla"));
		assertThat(scintilla.getArtist(),is("Stendeck"));
		assertThat(scintilla.getYear(),is("2011"));
		assertThat(scintilla.isAvailable(),is(true));
		
		assertThat(config.getLogfile(),is("hello.log"));
		assertThat(verbose,is(true));
	}
	
	
}
