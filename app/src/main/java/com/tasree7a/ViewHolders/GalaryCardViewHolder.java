package com.tasree7a.ViewHolders;

import android.view.View;
import android.widget.ImageView;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.R;

import java.util.ArrayList;

/**
 * Created by mac on 6/5/17.
 */

public class GalaryCardViewHolder extends BaseCardViewHolder {

    ImageView seeAll;

    public GalaryCardViewHolder(View view, BaseCardModel cardModel) {

        super(view, cardModel);

        seeAll = (ImageView) view.findViewById(R.id.see_all);

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ImageModel> imageModels = new ArrayList<>();

                String[] pathes = new String[]{
                        "http://womanandhome.media.ipcdigital.co.uk/21348/000016b24/f41d_orh100000w570/party-beauty-resized.jpg",
                        "http://i4.mirror.co.uk/incoming/article8424648.ece/ALTERNATES/s615/Amber-Heard.jpg",
                        "http://www.newsofbahrain.com/admin/post/upload/000PST_31-03-2016_1459426231_bYViJTGH2j.jpg",
                        "http://www.wonderslist.com/wp-content/uploads/2016/02/Nana-Im-Jin-Ah-Most-Beautiful-woman-2016.jpg",
                        "http://4.bp.blogspot.com/-UBAU9HcV66A/UzBHRxJ5ytI/AAAAAAAAKpE/Xexw0raBPn0/s1600/tamanna-bhatia-good-looking-hd-wallpapers.jpg.jpg",
                        "http://1.bp.blogspot.com/-nqbX2zc5EEs/UmJjN4mXfVI/AAAAAAAALpY/OnSA-3IbG3A/s1600/wide-miranda-kerr-beautiful-full-hd-5279-nonawalls.jpg",
                        "http://i44.tinypic.com/1zlx8cp.jpg",
                        "http://womanandhome.media.ipcdigital.co.uk/21348/000016b24/f41d_orh100000w570/party-beauty-resized.jpg",
                        "http://i4.mirror.co.uk/incoming/article8424648.ece/ALTERNATES/s615/Amber-Heard.jpg",
                        "http://www.newsofbahrain.com/admin/post/upload/000PST_31-03-2016_1459426231_bYViJTGH2j.jpg",
                        "http://www.wonderslist.com/wp-content/uploads/2016/02/Nana-Im-Jin-Ah-Most-Beautiful-woman-2016.jpg",
                        "http://4.bp.blogspot.com/-UBAU9HcV66A/UzBHRxJ5ytI/AAAAAAAAKpE/Xexw0raBPn0/s1600/tamanna-bhatia-good-looking-hd-wallpapers.jpg.jpg",
                        "http://1.bp.blogspot.com/-nqbX2zc5EEs/UmJjN4mXfVI/AAAAAAAALpY/OnSA-3IbG3A/s1600/wide-miranda-kerr-beautiful-full-hd-5279-nonawalls.jpg",
                        "http://i44.tinypic.com/1zlx8cp.jpg"
                };

                for(int i = 0 ; i < pathes.length ; i++){

                    ImageModel imageModel = new ImageModel();

                    imageModel.setImagePath(pathes[i]);

                    imageModels.add(imageModel);

                }

                FragmentManager.showFragmentGallery(imageModels);

            }
        });

    }
}
