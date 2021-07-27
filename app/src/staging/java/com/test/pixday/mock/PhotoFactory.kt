package com.test.pixday.mock

import com.test.pixday.model.Assets
import com.test.pixday.model.Media
import com.test.pixday.model.Photo

class PhotoFactory(val photoList: List<Photo> = arrayListOf()) {
    companion object {
        private val preview = Media(
            300,
            "https://image.shutterstock.com/display_pic_with_logo/223265567/454107526/stock-photo-cheerful-pretty-young-woman-in-hat-sitting-and-hugging-her-dog-on-the-beach-454107526.jpg",
            450
        )
        val photo = Photo(
            "454107526",
            1.4999,
            "Cheerful pretty young woman in hat sitting and hugging her dog on the beach",
            Assets(preview)
        )

        private val preview2 = Media(
            300,
            "https://image.shutterstock.com/display_pic_with_logo/283466185/1846008106/stock-photo-a-close-up-shot-a-dog-walked-by-its-owner-on-a-beach-near-ocean-1846008106.jpg",
            450
        )
        val photo2 = Photo(
            "1846008106",
            1.5,
            "A close up shot a dog walked by its owner on a beach near ocean.",
            Assets(preview2)
        )
    }
}