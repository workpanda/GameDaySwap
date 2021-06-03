package com.hometown.sports;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreateChallenge extends MenuBarActivity {

	Button makeChal;
	Spinner awayTeams, homeTeams;
	ArrayList<String> homeTeamList, awayTeamList;
	ArrayAdapter<String> awayAdapter;
	private ProgressDialog pDialog;
	int[] homeTeamIDs = new int[100];
	int[] awayTeamIDs = new int[100];
	int[] homeRosterIDs = new int[100];
	int[] awayRosterIDs = new int[100];
	// all active players below

	
	Activity useActivity;
	Roster useRoster, useRoster2, useRoster3, useRoster4;
	Roster homeSelected;
	Roster awaySelected;
	int sport;
	ImageView imagevw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_challenge);
		
		Intent in = getIntent();
		sport = in.getIntExtra("sportType", 0);
		
		imagevw = (ImageView) findViewById(R.id.sportLogoCreate);
		
		switch (sport) {
		case NFL_ID:
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_footballnfl", "drawable",
							getPackageName())));
			break;
		case NHL_ID:
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier("drawable/" + "ic_hockey",
							"drawable", getPackageName())));
			break;
		case NBA_ID:
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_basketballnba", "drawable",
							getPackageName())));
			break;
		case MLB_ID:
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier("drawable/" + "ic_baseball",
							"drawable", getPackageName())));
			break;
		case NCAAF_ID:
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_footballncaa", "drawable",
							getPackageName())));
			break;
		case NCAAB_ID:
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_basketballncaa", "drawable",
							getPackageName())));
			break;
		default:
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_launcher", "drawable",
							getPackageName())));
			break;

		}
		
		
		homeTeams = (Spinner) findViewById(R.id.spinHomeList);
		awayTeams = (Spinner) findViewById(R.id.spinAwayList);
		makeChal = (Button) findViewById(R.id.makeChalButton);
		
		//teams = getCurrentTeamList();
		
		homeTeamList = new ArrayList<String>();
		awayTeamList = new ArrayList<String>();
		for (int i = 0; i < currentRosterList.getRosters(); i++) {
			Roster r = currentRosterList.getRoster(i);
			homeRosterIDs[i] = r.getID();
			Team t = currentTeamList.getTeamByID(r.getTeamID());
			String addName = t.getDisplayName();
			homeTeamList.add(addName);
			homeTeamIDs[i] = t.getTeamID();
		}

		ArrayList<String> awayTeamList2 = new ArrayList<String>();
		awayTeamList2.add("Pick Away Team");

		final ArrayAdapter<String> homeAdapter = new ArrayAdapter<String>(this,
				R.layout.spinnerlayout, homeTeamList);
		final ArrayAdapter<String> awayAdapter2 = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, awayTeamList2);
		homeTeams.setAdapter(homeAdapter);
		// awayTeams.setAdapter(awayAdapter2);

		createSpin2(0);

		awayTeams.setAdapter(awayAdapter);

		homeTeams.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				createSpin2(pos);

				awayTeams.setAdapter(awayAdapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		makeChal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				homeSelected = currentRosterList.getRosterByID(homeRosterIDs[homeTeams.getSelectedItemPosition()]);
				awaySelected = currentRosterList.getRosterByID(awayRosterIDs[awayTeams.getSelectedItemPosition()]);
				
				//need to add find other two rosters to this section and re route to single game with these teams probably set currentGame

				/* remove these lines after re-direct to single game activity
				RosterDisplay show = new RosterDisplay(CreateChallenge.this, "Confirm Rosters",
						homeSelected, awaySelected, false);
				
				show.execute();
				*/

			}
		});
		
		

	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Create a Challenge:";
		String txt = "Select two rosters to create a challenge";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	public void createSpin2(int pos) {
		awayTeamList.clear();
		int selectedID = homeRosterIDs[pos];
		
		for (int i = 0; i < currentRosterList.getRosters(); i++) {

			Roster r = currentRosterList.getRoster(i);
			Team t = currentTeamList.getTeamByID(r.getTeamID());
				if (r.getID()!=selectedID) {
					String addName = t.getDisplayName();
					awayTeamList.add(addName);
					awayTeamIDs[i] = t.getTeamID();
					awayRosterIDs[i] = r.getID();
				}
			
		}

		awayAdapter = new ArrayAdapter<String>(this, R.layout.spinnerlayout,
				awayTeamList);
	}
	
	
	

	/*
	 * 
	Roster Bears = new Roster(34, "Bears", 10);
	Roster Bengals = new Roster(35, "Bengals", 10);
	Roster Bills = new Roster(24, "Bills", 10);
	Roster Browns = new Roster(45, "Browns", 10);
	
	
	public void createRosters() {
		Player p1 = new Player("Sam Acho", "S", "CHI", 1, 1);
		Player p2 = new Player("Jared Allen", "DE", "CHI", 2, 1);
		Player p3 = new Player("David Bass", "DE", "CHI", 3, 2);
		Player p4 = new Player("Martellus Bennett", "TE", "CHI", 4, 1);
		Player p5 = new Player("Jermon Bushrod", "OT", "CHI", 5, 2);
		Player p6 = new Player("Ka'Deem Carey", "RB", "CHI", 6, 1);
		Player p7 = new Player("Jimmy Clausen", "QB", "CHI", 7, 1);
		Player p8 = new Player("Jay Cutler", "QB", "CHI", 8, 1);
		Player p9 = new Player("Vladamir Ducasse", "OG", "CHI", 9, 5);
		Player p10 = new Player("David Fales", "QB", "CHI", 10, 1);
		Player p11 = new Player("Ego Ferguson", "DT", "CHI", 11, 1);
		Player p12 = new Player("Matt Forte", "RB", "CHI", 12, 1);
		Player p13 = new Player("Kyle Fuller", "CB", "CHI", 13, 1);
		Player p14 = new Player("Robbie Gould", "K", "CHI", 14, 1);
		Player p15 = new Player("Antrelle Rolle", "S", "CHI", 15, 1);
		Player p16 = new Player("Eddie Royal", "WR", "CHI", 16, 1);
		Player p17 = new Player("Marquess Williams", "WR", "CHI", 17, 1);
		Player p18 = new Player("Mario Alford", "WR", "CIN", 18, 1);
		Player p19 = new Player("Geno Atkins", "DT", "CIN", 19, 1);
		Player p20 = new Player("Giovani Bernard", "RB", "CIN", 20, 1);
		Player p21 = new Player("Russell Bodine", "C", "CIN", 21, 1);
		Player p22 = new Player("Clink Boling", "G", "CIN", 22, 1);
		Player p23 = new Player("Vontaze Burfict", "LB", "CIN", 23, 1);
		Player p24 = new Player("Andy Dalton", "QB", "CIN", 24, 1);
		Player p25 = new Player("Erick Dargan", "S", "CIN", 25, 2);
		Player p26 = new Player("Darqueze Dennard", "CB", "CIN", 26, 1);
		Player p27 = new Player("Tyler Eifert", "TE", "CIN", 27, 1);
		Player p28 = new Player("Jake Fisher", "OT", "CIN", 28, 2);
		Player p29 = new Player("Leon Hall", "CB", "CIN", 29, 1);
		Player p30 = new Player("Trey Hopkins", "G", "CIN", 30, 5);
		Player p31 = new Player("George Iloka", "S", "CIN", 31, 1);
		Player p32 = new Player("Michael Johnson", "DE", "CIN", 32, 1);
		Player p33 = new Player("Domata Peko", "DT", "CIN", 33, 1);
		Player p34 = new Player("Terrelle Pryor", "QB", "CIN", 34, 1);
		Player p35 = new Player("Josh Shaw", "CB", "CIN", 35, 1);
		Player p36 = new Player("Brandon Tate", "WR", "CIN", 36, 1);
		Player p37 = new Player("Andrew Whitworth", "OT", "CIN", 37, 1);
		Player p38 = new Player("Nigel Bradham", "LB", "BUF", 38, 1);
		Player p39 = new Player("Deon Broomfield", "S", "BUF", 39, 1);
		Player p40 = new Player("Justin Brown", "WR", "BUF", 40, 1);
		Player p41 = new Player("William Campbell", "G", "BUF", 41, 2);
		Player p42 = new Player("Dan Carpenter", "K", "BUF", 42, 1);
		Player p43 = new Player("Tyson Chandler", "OT", "BUF", 43, 2);
		Player p44 = new Player("Charles Clay", "TE", "BUF", 44, 1);
		Player p45 = new Player("John Connor", "FB", "BUF", 45, 1);
		Player p46 = new Player("Marcel Darius", "DT", "BUF", 46, 1);
		Player p47 = new Player("Jordan Gay", "K", "BUF", 47, 5);
		Player p48 = new Player("Justin Hamilton", "DT", "BUF", 48, 1);
		Player p49 = new Player("Andrew Hudwon", "LB", "BUF", 49, 1);
		Player p50 = new Player("Fred Jackson", "RB", "BUF", 50, 1);
		Player p51 = new Player("Cyrus Kouandjio", "OT", "BUF", 51, 1);
		Player p52 = new Player("EJ Mannuel", "QB", "BUF", 52, 1);
		Player p53 = new Player("LeSean McCoy", "RB", "BUF", 53, 1);
		Player p54 = new Player("Nickell Robey", "CB", "BUF", 54, 1);
		Player p55 = new Player("Tony Steward", "LB", "BUF", 55, 1);
		Player p56 = new Player("Sammy Watkins", "WR", "BUF", 56, 1);
		Player p57 = new Player("Robert Woods", "WR", "BUF", 57, 1);
		Player p58 = new Player("Johnson Bademosi", "DB", "CLE", 58, 1);
		Player p59 = new Player("Travis Benjamin", "WR", "CLE", 59, 1);
		Player p60 = new Player("Armonty Bryant", "DL", "CLE", 60, 1);
		Player p61 = new Player("Tank Carder", "LB", "CLE", 61, 1);
		Player p62 = new Player("Ifo Ekpre-Olomu", "CB", "CLE", 62, 1);
		Player p63 = new Player("Cameron Erving", "OL", "CLE", 63, 2);
		Player p64 = new Player("Landon Feichter", "DB", "CLE", 64, 1);
		Player p65 = new Player("Charles Gains", "DB", "CLE", 65, 1);
		Player p66 = new Player("Justin Gilbert", "CB", "CLE", 66, 1);
		Player p67 = new Player("John Greco", "OL", "CLE", 67, 5);
		Player p68 = new Player("Andrew Hawkins", "WR", "CLE", 68, 1);
		Player p69 = new Player("Darius Jennings", "WR", "CLE", 69, 1);
		Player p70 = new Player("Tyler Loos", "OL", "CLE", 70, 2);
		Player p71 = new Player("Johnny Manziel", "QB", "CLE", 71, 1);
		Player p72 = new Player("Rodman Noel", "LB", "CLE", 72, 1);
		Player p73 = new Player("Micah Pellerin", "DB", "CLE", 73, 1);
		Player p74 = new Player("De'Antre Saunders", "DB", "CLE", 74, 1);
		Player p75 = new Player("Carey Spear", "K", "CLE", 75, 1);
		Player p76 = new Player("Randy Starks", "DL", "CLE", 76, 1);
		Player p77 = new Player("Randall Telfer", "TE", "CLE", 77, 1);
		Player p78 = new Player("Joe Thomas", "OT", "CLE", 78, 1);
		Player p79 = new Player("Billy Winn", "DL", "CLE", 79, 1);
		Player p80 = new Player("David Bass", "DE", "CHI", 80, 6);
		Player p81 = new Player("Jermon Bushrod", "OT", "CHI", 81, 6);
		Player p82 = new Player("Vladamir Ducasse", "OG", "CHI", 82, 3);
		Player p83 = new Player("Erick Dargan", "S", "CIN", 83, 6);
		Player p84 = new Player("Jake Fisher", "OT", "CIN", 84, 6);
		Player p85 = new Player("Trey Hopkins", "G", "CIN", 85, 3);
		Player p86 = new Player("William Campbell", "G", "BUF", 86, 6);
		Player p87 = new Player("Tyson Chandler", "OT", "BUF", 87, 6);
		Player p88 = new Player("Jordan Gay", "K", "BUF", 88, 3);
		Player p89 = new Player("Cameron Erving", "OL", "CLE", 89, 6);
		Player p90 = new Player("John Greco", "OL", "CLE", 90, 3);
		Player p91 = new Player("Tyler Loos", "OL", "CLE", 91, 6);

		Bears.push(p1);
		Bears.push(p2);
		Bears.push(p3);
		Bears.push(p4);
		Bears.push(p5);
		Bears.push(p6);
		Bears.push(p7);
		Bears.push(p8);
		Bears.push(p9);
		Bears.push(p10);
		Bears.push(p11);
		Bears.push(p12);
		Bears.push(p13);
		Bears.push(p14);
		Bears.push(p15);
		Bears.push(p16);
		Bears.push(p17);
		Bengals.push(p18);
		Bengals.push(p19);
		Bengals.push(p20);
		Bengals.push(p21);
		Bengals.push(p22);
		Bengals.push(p23);
		Bengals.push(p24);
		Bengals.push(p25);
		Bengals.push(p26);
		Bengals.push(p27);
		Bengals.push(p28);
		Bengals.push(p29);
		Bengals.push(p30);
		Bengals.push(p31);
		Bengals.push(p32);
		Bengals.push(p33);
		Bengals.push(p34);
		Bengals.push(p35);
		Bengals.push(p36);
		Bengals.push(p37);
		Bills.push(p38);
		Bills.push(p39);
		Bills.push(p40);
		Bills.push(p41);
		Bills.push(p42);
		Bills.push(p43);
		Bills.push(p44);
		Bills.push(p45);
		Bills.push(p46);
		Bills.push(p47);
		Bills.push(p48);
		Bills.push(p49);
		Bills.push(p50);
		Bills.push(p51);
		Bills.push(p52);
		Bills.push(p53);
		Bills.push(p54);
		Bills.push(p55);
		Bills.push(p56);
		Bills.push(p57);
		Browns.push(p58);
		Browns.push(p59);
		Browns.push(p60);
		Browns.push(p61);
		Browns.push(p62);
		Browns.push(p63);
		Browns.push(p64);
		Browns.push(p65);
		Browns.push(p66);
		Browns.push(p67);
		Browns.push(p68);
		Browns.push(p69);
		Browns.push(p70);
		Browns.push(p71);
		Browns.push(p72);
		Browns.push(p73);
		Browns.push(p74);
		Browns.push(p75);
		Browns.push(p76);
		Browns.push(p77);
		Browns.push(p78);
		Browns.push(p79);
		Bengals.push(p80);
		Bengals.push(p81);
		Bengals.push(p82);
		Bears.push(p83);
		Bears.push(p84);
		Bears.push(p85);
		Browns.push(p86);
		Browns.push(p87);
		Browns.push(p88);
		Bills.push(p89);
		Bills.push(p90);
		Bills.push(p91);

		teamNames[0] = "Bears";
		teamNames[1] = "Bengals";
		teamNames[2] = "Bills";
		teamNames[3] = "Browns";

	}
	*/
}
