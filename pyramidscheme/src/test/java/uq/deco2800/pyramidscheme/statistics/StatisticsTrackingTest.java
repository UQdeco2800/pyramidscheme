package uq.deco2800.pyramidscheme.statistics;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by billy on 22/10/16.
 */
public class StatisticsTrackingTest {

	@Test
	public void createGuestTest() {
		StatisticsTracking stats = new StatisticsTracking();
		stats.createGuestUser();
		
		Assert.assertEquals("Guest", stats.getUserStats().getUserID());
		Assert.assertEquals("1", stats.getUserStats().getUserLevel());
		Assert.assertEquals("0", stats.getUserStats().getMinionsPlayed());
		Assert.assertEquals("0", stats.getUserStats().getMinionsLost());
		Assert.assertEquals("0", stats.getUserStats().getMinionsKilled());
		Assert.assertEquals("0", stats.getUserStats().getHealthTaken());
		Assert.assertEquals("0", stats.getUserStats().getHealthLost());
		Assert.assertEquals("0", stats.getUserStats().getTotalWins());
		Assert.assertEquals("0", stats.getUserStats().getTotalLosses());
		Assert.assertEquals("0", stats.getUserStats().getTotalHours());
		Assert.assertEquals("0", stats.getUserStats().getTotalMinutes());
		
		Assert.assertEquals("Guest", stats.getRallardStats().getUserID());
		Assert.assertEquals(StatisticsTracking.RALLARD_NAME, stats.getRallardStats().getChampName());
		Assert.assertEquals("0", stats.getRallardStats().getMinionsPlayed());
		Assert.assertEquals("0", stats.getRallardStats().getMinionsLost());
		Assert.assertEquals("0", stats.getRallardStats().getMinionsKilled());
		Assert.assertEquals("0", stats.getRallardStats().getHealthTaken());
		Assert.assertEquals("0", stats.getRallardStats().getHealthLost());
		Assert.assertEquals("0", stats.getRallardStats().getTotalWins());
		Assert.assertEquals("0", stats.getRallardStats().getTotalLosses());
		Assert.assertEquals("0", stats.getRallardStats().getTotalHours());
		Assert.assertEquals("0", stats.getRallardStats().getTotalMinutes());
		
		Assert.assertEquals("Guest", stats.getQuackubisStats().getUserID());
		Assert.assertEquals(StatisticsTracking.QUACKUBIS_NAME, stats.getQuackubisStats().getChampName());
		Assert.assertEquals("0", stats.getQuackubisStats().getMinionsPlayed());
		Assert.assertEquals("0", stats.getQuackubisStats().getMinionsLost());
		Assert.assertEquals("0", stats.getQuackubisStats().getMinionsKilled());
		Assert.assertEquals("0", stats.getQuackubisStats().getHealthTaken());
		Assert.assertEquals("0", stats.getQuackubisStats().getHealthLost());
		Assert.assertEquals("0", stats.getQuackubisStats().getTotalWins());
		Assert.assertEquals("0", stats.getQuackubisStats().getTotalLosses());
		Assert.assertEquals("0", stats.getQuackubisStats().getTotalHours());
		Assert.assertEquals("0", stats.getQuackubisStats().getTotalMinutes());
		
		Assert.assertEquals("Guest", stats.getFowlSphinxStats().getUserID());
		Assert.assertEquals(StatisticsTracking.FOWLSPHINX_NAME, stats.getFowlSphinxStats().getChampName());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getMinionsPlayed());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getMinionsLost());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getMinionsKilled());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getHealthTaken());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getHealthLost());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getTotalWins());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getTotalLosses());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getTotalHours());
		Assert.assertEquals("0", stats.getFowlSphinxStats().getTotalMinutes());
		
		Assert.assertEquals("Guest", stats.getKhepriStats().getUserID());
		Assert.assertEquals(StatisticsTracking.KHEPRI_NAME, stats.getKhepriStats().getChampName());
		Assert.assertEquals("0", stats.getKhepriStats().getMinionsPlayed());
		Assert.assertEquals("0", stats.getKhepriStats().getMinionsLost());
		Assert.assertEquals("0", stats.getKhepriStats().getMinionsKilled());
		Assert.assertEquals("0", stats.getKhepriStats().getHealthTaken());
		Assert.assertEquals("0", stats.getKhepriStats().getHealthLost());
		Assert.assertEquals("0", stats.getKhepriStats().getTotalWins());
		Assert.assertEquals("0", stats.getKhepriStats().getTotalLosses());
		Assert.assertEquals("0", stats.getKhepriStats().getTotalHours());
		Assert.assertEquals("0", stats.getKhepriStats().getTotalMinutes());
	}
	
	@Test
	public void setStatIDTest() {
		StatisticsTracking stats = new StatisticsTracking();
		stats.createGuestUser();
		
		stats.setUserID("TEST");
		Assert.assertEquals("TEST", stats.getUserStats().getUserID());
		Assert.assertEquals("TEST", stats.getRallardStats().getUserID());
		Assert.assertEquals("TEST", stats.getQuackubisStats().getUserID());
		Assert.assertEquals("TEST", stats.getFowlSphinxStats().getUserID());
		Assert.assertEquals("TEST", stats.getKhepriStats().getUserID());
	}
	
	@Test
	public void championSwitchTest() {
		StatisticsTracking stats = new StatisticsTracking();
		stats.createGuestUser();
		
		//Default should be rallard
		Assert.assertEquals(StatisticsTracking.RALLARD_NAME, stats.getCurrentChamp().getChampName());
		
		stats.switchChampionTracking(StatisticsTracking.QUACKUBIS_NAME);
		Assert.assertEquals(StatisticsTracking.QUACKUBIS_NAME, stats.getCurrentChamp().getChampName());
		
		stats.switchChampionTracking(StatisticsTracking.FOWLSPHINX_NAME);
		Assert.assertEquals(StatisticsTracking.FOWLSPHINX_NAME, stats.getCurrentChamp().getChampName());
		
		stats.switchChampionTracking(StatisticsTracking.KHEPRI_NAME);
		Assert.assertEquals(StatisticsTracking.KHEPRI_NAME, stats.getCurrentChamp().getChampName());
		
		stats.switchChampionTracking(StatisticsTracking.RALLARD_NAME);
		Assert.assertEquals(StatisticsTracking.RALLARD_NAME, stats.getCurrentChamp().getChampName());
	}
	
	@Test
	public void addToIntTest() {
		StatisticsTracking stats = new StatisticsTracking();
		stats.createGuestUser();
		
		//Default is rallard
		stats.addToMinionsPlayed(1);
		stats.addToMinionsKilled(1);
		stats.addToMinionsLost(1);
		stats.addToHealthLost(1);
		stats.addToHealthTaken(1);
		stats.addToTotalWins(1);
		stats.addToTotalLosses(1);
		
		Assert.assertEquals("1", stats.getUserStats().getMinionsPlayed());
		Assert.assertEquals("1", stats.getUserStats().getMinionsKilled());
		Assert.assertEquals("1", stats.getUserStats().getMinionsLost());
		Assert.assertEquals("1", stats.getUserStats().getHealthLost());
		Assert.assertEquals("1", stats.getUserStats().getHealthTaken());
		Assert.assertEquals("1", stats.getUserStats().getTotalWins());
		Assert.assertEquals("1", stats.getUserStats().getTotalLosses());
		
		Assert.assertEquals("1", stats.getRallardStats().getMinionsPlayed());
		Assert.assertEquals("1", stats.getRallardStats().getMinionsKilled());
		Assert.assertEquals("1", stats.getRallardStats().getMinionsLost());
		Assert.assertEquals("1", stats.getRallardStats().getHealthLost());
		Assert.assertEquals("1", stats.getRallardStats().getHealthTaken());
		Assert.assertEquals("1", stats.getRallardStats().getTotalWins());
		Assert.assertEquals("1", stats.getRallardStats().getTotalLosses());
	}
	
	@Test
	public void addToStringTest() {
		StatisticsTracking stats = new StatisticsTracking();
		stats.createGuestUser();
		
		//Default is rallard
		stats.addToMinionsPlayed("1");
		stats.addToMinionsKilled("1");
		stats.addToMinionsLost("1");
		stats.addToHealthLost("1");
		stats.addToHealthTaken("1");
		stats.addToTotalWins("1");
		stats.addToTotalLosses("1");
		
		Assert.assertEquals("1", stats.getUserStats().getMinionsPlayed());
		Assert.assertEquals("1", stats.getUserStats().getMinionsKilled());
		Assert.assertEquals("1", stats.getUserStats().getMinionsLost());
		Assert.assertEquals("1", stats.getUserStats().getHealthLost());
		Assert.assertEquals("1", stats.getUserStats().getHealthTaken());
		Assert.assertEquals("1", stats.getUserStats().getTotalWins());
		Assert.assertEquals("1", stats.getUserStats().getTotalLosses());
		
		Assert.assertEquals("1", stats.getRallardStats().getMinionsPlayed());
		Assert.assertEquals("1", stats.getRallardStats().getMinionsKilled());
		Assert.assertEquals("1", stats.getRallardStats().getMinionsLost());
		Assert.assertEquals("1", stats.getRallardStats().getHealthLost());
		Assert.assertEquals("1", stats.getRallardStats().getHealthTaken());
		Assert.assertEquals("1", stats.getRallardStats().getTotalWins());
		Assert.assertEquals("1", stats.getRallardStats().getTotalLosses());
	}
	
	@Test
	public void setStatisticsTest() {
		StatisticsTracking stats = new StatisticsTracking();
		stats.createGuestUser();
		stats.getUserStats().setStatID("test");
		stats.getRallardStats().setStatID("test");
		stats.getQuackubisStats().setStatID("test");
		stats.getFowlSphinxStats().setStatID("test");
		stats.getKhepriStats().setStatID("test");
		
		StatisticsTracking stats2 = new StatisticsTracking();
		stats2.createGuestUser();
		stats2.getUserStats().setStatID("test");
		stats2.getRallardStats().setStatID("test");
		stats2.getQuackubisStats().setStatID("test");
		stats2.getFowlSphinxStats().setStatID("test");
		stats2.getKhepriStats().setStatID("test");
		
		
		stats2.setUserID("Stats2");
		
		stats.setUserStats(stats2.getUserStats());
		Assert.assertEquals(stats.getUserStats(), stats2.getUserStats());
		
		stats.setRallardStats(stats2.getRallardStats());
		Assert.assertEquals(stats.getRallardStats(), stats2.getRallardStats());
		
		stats.setQuackubisStats(stats2.getQuackubisStats());
		Assert.assertEquals(stats.getQuackubisStats(), stats2.getQuackubisStats());
		
		stats.setFowlSphinxStats(stats2.getFowlSphinxStats());
		Assert.assertEquals(stats.getFowlSphinxStats(), stats2.getFowlSphinxStats());
		
		stats.setKhepriStats(stats2.getKhepriStats());
		Assert.assertEquals(stats.getKhepriStats(), stats2.getKhepriStats());
	}
}