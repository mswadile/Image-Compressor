// Generated by view binder compiler. Do not edit!
package com.example.imagecompressor.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.imagecompressor.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCompressBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView belowImgTxt;

  @NonNull
  public final TextView comLbl;

  @NonNull
  public final Button compressBtn;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final TextView imgInfoTxt;

  @NonNull
  public final Button uploadBtn1;

  private ActivityCompressBinding(@NonNull ConstraintLayout rootView, @NonNull TextView belowImgTxt,
      @NonNull TextView comLbl, @NonNull Button compressBtn, @NonNull ImageView imageView,
      @NonNull TextView imgInfoTxt, @NonNull Button uploadBtn1) {
    this.rootView = rootView;
    this.belowImgTxt = belowImgTxt;
    this.comLbl = comLbl;
    this.compressBtn = compressBtn;
    this.imageView = imageView;
    this.imgInfoTxt = imgInfoTxt;
    this.uploadBtn1 = uploadBtn1;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCompressBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCompressBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_compress, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCompressBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.belowImgTxt;
      TextView belowImgTxt = ViewBindings.findChildViewById(rootView, id);
      if (belowImgTxt == null) {
        break missingId;
      }

      id = R.id.comLbl;
      TextView comLbl = ViewBindings.findChildViewById(rootView, id);
      if (comLbl == null) {
        break missingId;
      }

      id = R.id.compressBtn;
      Button compressBtn = ViewBindings.findChildViewById(rootView, id);
      if (compressBtn == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.imgInfoTxt;
      TextView imgInfoTxt = ViewBindings.findChildViewById(rootView, id);
      if (imgInfoTxt == null) {
        break missingId;
      }

      id = R.id.uploadBtn1;
      Button uploadBtn1 = ViewBindings.findChildViewById(rootView, id);
      if (uploadBtn1 == null) {
        break missingId;
      }

      return new ActivityCompressBinding((ConstraintLayout) rootView, belowImgTxt, comLbl,
          compressBtn, imageView, imgInfoTxt, uploadBtn1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}