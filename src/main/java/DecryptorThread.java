import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FilenameFilter;

public class DecryptorThread extends Thread {

    private GUIForm form;
    private File file;
    private String password;

    public DecryptorThread(GUIForm form) {
        this.form = form;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String getOutputPath() {
        String path = file.getAbsolutePath()
                .replaceAll("\\.enc$", "");
        for (int i = 0; ; i++) {
            String number = i >= 1 ? Integer.toString(i) : "";
            String outPath = path + number;
            if (!new File(path + number).exists()) {
                return outPath;
            }
        }
    }

    @Override
    public void run() {
        onStart();
        try {
            String outPath = getOutputPath();
            ZipFile zipFile = new ZipFile(file);
            zipFile.setPassword(password);
            zipFile.extractAll(outPath);
            onFinish();
        } catch (ZipException e) {
            form.showWarning(e.getCode() + " " + "Wrong password");
            form.setButtonsEnabled(true);
        }
    }

    private void onStart() {
        form.setButtonsEnabled(false);
    }

    private void onFinish() {
        form.setButtonsEnabled(true);
        form.showFinished();
    }
}
