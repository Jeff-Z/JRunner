package cn.hartech.jrunner.ui.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import cn.hartech.jrunner.R;
import cn.hartech.jrunner.RunningActivity;

/**
 * ֹͣ��¼ʱ��ʾ�Ƿ񱣴�
 * 
 * @author jin.zheng
 * @date 2012��12��14��
 * 
 */
@SuppressLint("ValidFragment")
public class DialogStopRunning extends DialogFragment {

	private RunningActivity runningActivity;

	public DialogStopRunning(RunningActivity runningActivity) {

		this.runningActivity = runningActivity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		return new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.ic_launcher)
				.setTitle("�Ƿ񱣴棿")
				.setPositiveButton("��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						runningActivity.stopRunningWithoutSave();
					}
				})
				.setNegativeButton("��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						runningActivity.stopRunningAndSave();

					}
				}).create();
	}
}
