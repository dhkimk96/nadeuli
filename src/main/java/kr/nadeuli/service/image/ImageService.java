package kr.nadeuli.service.image;


import kr.nadeuli.dto.ImageDTO;
import kr.nadeuli.dto.SearchDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void addImage(List<MultipartFile> multipartFiles,ImageDTO imageDTO) throws Exception;

    ImageDTO getImage(long imageId) throws Exception;

    List<ImageDTO> getImageList(long id, SearchDTO searchDTO) throws Exception;

    void deletePostImage(long postId) throws Exception;
    void deleteProductImage(long productId) throws Exception;
    void deleteNadeuliDeliveryImage(long nadeuliDeliveryId) throws Exception;

    void deleteImage(long imageId) throws Exception;

}
