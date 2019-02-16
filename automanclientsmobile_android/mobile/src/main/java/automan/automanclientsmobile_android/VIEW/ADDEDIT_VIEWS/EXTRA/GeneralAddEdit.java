package automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.EXTRA;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;

/**
 * Created by F on 6/18/2017.
 */
public class GeneralAddEdit {

    private AutomanEnumerations addeditOpt = null;
    private String addeditId = "";
    private AutoObject addeditObj = null;
    private String addedit = "";

    public AutoObject getObj() {
        return this.addeditObj;
    }

    private Activity addeditAct = null;
    private Intent i = null;

    private static GeneralAddEdit generalAddEdit = new GeneralAddEdit();

    public static GeneralAddEdit get() {
        return generalAddEdit;
    }

    public AutoObject genericSetUp(AutomanEnumerations opt, Activity act, Boolean isUserProfile) {
        if(isUserProfile){
            this.addeditOpt = opt;
            this.addeditAct = act;
            this.addeditObj = AutomanApp.getApp().getUser();
            return this.addeditObj;
        } else return null;
    }

    public AutoObject genericSetUp(AutomanEnumerations opt, Activity act) {
        this.addeditOpt = opt;
        this.addeditAct = act;

        this.addedit = this.addeditAct.getIntent().getExtras().getString("addoredit");
        this.addeditId = this.addeditAct.getIntent().getExtras().getString("id");
        //
        if(this.addedit.equalsIgnoreCase("edit") && this.addeditId.length() > 0)
            this.addeditObj = AutomanApp.getApp().find(this.addeditOpt, addeditId);
        else this.addeditObj = new AutoObject();
        if(this.addeditObj == null) this.addeditObj = new AutoObject();
        //
        Log.e(this.addeditOpt.toSingularUpperCase(this.addeditOpt), "ADD/EDIT (" + this.addedit.toUpperCase() + ") PAGE -> " + this.addeditObj.toJSON(true, null).toString());
        if( (this.addedit.equalsIgnoreCase("edit")) && (this.addeditObj.id() != null) && (this.addeditObj.id().length() > 0) )
            return this.addeditObj;
        else return null;
    }

    public void genericGetImage(String img, ImageView imgview) {
        AutomanApp.getApp().getImage(this.addeditOpt, img, this.addeditAct, imgview);
    }

    private Bitmap img = null;

    public Bitmap img() {
        return this.img;
    }

    public void img(Bitmap img) {
        this.img = img;
    }

    public static void useCameraOrGallery(final Activity act) {
        /// SHOW DIALOG FOR USER TO CHOOSE FROM GALLERY OR TAKE A PICTURE OF CHEQUE
        AlertDialog.Builder d = new AlertDialog.Builder(AutomanApp.getApp().getApplicationContext());
        d.setMessage("Choose Option");
        d.setCancelable(true);
        d.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                act.startActivityForResult(i, 0);
            }
        });
        d.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                act.startActivityForResult(Intent.createChooser(i, "Choose Image"), 1);
            }
        });
        AlertDialog a = d.create();
        a.setTitle("Take Photo, or Choose from Gallery");
        a.show();
    }
}
