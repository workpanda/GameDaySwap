package com.hometown.sports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BottomBar extends Fragment {

	ImageButton goHome, goPending, goActive, goHelp, goChallenge;
	BottomListener activityCallback;
	int chalType;
	String title;
	String text;

	public interface BottomListener {
		// public void onEmailLogInClick(String email, String pw);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			activityCallback = (BottomListener) activity;
		} catch (ClassCastException e) {
			Log.d("bottom bar on Attach", "class cast");
		}
	}

	public static BottomBar newInstance(int chalType, String ttl, String txt) {
		BottomBar myFragment = new BottomBar();

		Bundle args = new Bundle();
		args.putInt("chalType", chalType);
		args.putString("title", ttl);
		args.putString("text", txt);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottombarfrag, container, false);
		goHome = (ImageButton) view.findViewById(R.id.goHome);
		goPending = (ImageButton) view.findViewById(R.id.goPending);
		goChallenge = (ImageButton) view.findViewById(R.id.challenge);
		goActive = (ImageButton) view.findViewById(R.id.goActive);
		goHelp = (ImageButton) view.findViewById(R.id.getInfo);

		if (getArguments() != null) {
			chalType = getArguments().getInt("chalType", 0);
			title = getArguments().getString("title");
			text = getArguments().getString("text");
		}

		goHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goHomeClicked(v);
			}
		});

		goPending.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goPendingClicked(v);
			}
		});
		goActive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goActiveClicked(v);
			}
		});
		goChallenge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goChallengeClicked(v);
			}
		});
		goHelp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				title = MenuBarActivity.getBottomTitle();
				text = MenuBarActivity.getBottomText();
				goHelpClicked(v);
			}
		});
		return view;
	}

	private void goHomeClicked(View v) {
		// TODO Auto-generated method stub
		Activity a = getActivity();
		if (a instanceof HomeTownSportsHome) {
			Log.d("goHomeClicked", "already at home");
		} else {
			Intent in1 = new Intent(a, HomeTownSportsHome.class);
			startActivity(in1);
		}

	}

	private void goPendingClicked(View v) {
		Activity a = getActivity();
		if (a instanceof Pendingchallenges) {
			Log.d("goPendingClicked", "already at pending");
		} else {
			Intent in1 = new Intent(a, Pendingchallenges.class);
			startActivity(in1);
		}
	}

	private void goActiveClicked(View v) {
		Activity a = getActivity();
		if (a instanceof UserChalListPage
				&& MenuBarActivity.getBottomInt() == MenuBarActivity.CHAL_STATUS_ALLACCEPTED) {
			Log.d("goActiveClicked", "already at active");
		} else {
			Intent in1 = new Intent(a, UserChalListPage.class);
			in1.putExtra("chalType", MenuBarActivity.CHAL_STATUS_ALLACCEPTED);
			startActivity(in1);
		}

	}

	private void goHelpClicked(View v) {
		Activity a = getActivity();
		showDialog(a, title, text);

	}

	private void goChallengeClicked(View v) {
		Activity a = getActivity();
		Intent intent1 = new Intent(a, ChallengePage.class);
		startActivity(intent1);

	}

	public void showDialog(Activity activity, String ttle, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(ttle);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close box
			}
		});
		builder.show();
	}

}
