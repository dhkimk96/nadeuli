package kr.nadeuli.image;

import kr.nadeuli.dto.*;
import kr.nadeuli.service.image.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageApplicationTests {
    @Autowired
    ImageService imageService;


    @Value("${pageSize}")
    private int pageSize;


//    @Test
    public void testAddImageList() throws Exception {
        ImageDTO imageDTO = ImageDTO.builder()
                .imageName("testImg1")
                .post(PostDTO.builder().postId(2L).build())
                .product(ProductDTO.builder().productId(1L).build())
                .nadeuliDelivery(NadeuliDeliveryDTO.builder().nadeuliDeliveryId(2L).build())
                .build();
        imageService.addImage(imageDTO);
    }


//        @Test
//    @Transactional
    public void getImage() throws Exception {
        long imageId = 13L;
        System.out.println(imageService.getImage(imageId));;

    }



//        @Test
//        @Transactional
    public void testGetImagesList() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setCurrentPage(0);
        searchDTO.setPageSize(pageSize);
        long id = 2L;
        System.out.println(imageService.getImageList(id, searchDTO));
    }


    @Test
    public void testDeleteImage() throws Exception {
        imageService.deleteProductImage(10L);
    }
}
