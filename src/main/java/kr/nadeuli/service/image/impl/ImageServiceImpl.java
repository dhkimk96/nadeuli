package kr.nadeuli.service.image.impl;

import kr.nadeuli.dto.ImageDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.entity.Image;
import kr.nadeuli.entity.NadeuliDelivery;
import kr.nadeuli.entity.Post;
import kr.nadeuli.entity.Product;
import kr.nadeuli.mapper.ImageMapper;
import kr.nadeuli.service.image.ImageRepository;
import kr.nadeuli.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Transactional
@Service("imageServiceImpl")
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Value("${imageNum}")
    int imageNum;

    @Override
    public void addImage(ImageDTO imageDTO) throws Exception {
        Image image = imageMapper.imageDTOToImage(imageDTO);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = now.format(formatter);
        String imageName = image.getImageName();
        String updatedImageName = imageName + formattedDateTime;
        image.setImageName(updatedImageName);

        log.info(image);

        imageRepository.save(image);
    }

    @Override
    public ImageDTO getImage(long imageId) throws Exception {
        return imageRepository.findById(imageId).map(imageMapper::imageToImageDTO).orElse(null);
    }

    @Override
    public List<ImageDTO> getImageList(long id, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(0, imageNum);
        Page<Image> imagePage;
        if(searchDTO.isPost())
            imagePage = imageRepository.findByPost(Post.builder().postId(id).build(), pageable);
        else if(searchDTO.isProduct())
            imagePage = imageRepository.findByProduct(Product.builder().productId(id).build(), pageable);
        else {
            imagePage = imageRepository.findByNadeuliDelivery(NadeuliDelivery.builder().nadeuliDeliveryId(id).build(), pageable);
        }
        log.info(imagePage);
        return imagePage.map(imageMapper::imageToImageDTO).toList();
    }

    @Override
    public void deletePostImage(long postId) throws Exception {
            log.info(postId);
            imageRepository.deleteByPost(Post.builder().postId(postId).build());
    }

    @Override
    public void deleteProductImage(long productId) throws Exception {
        log.info(productId);
        imageRepository.deleteByProduct(Product.builder().productId(productId).build());
    }

    @Override
    public void deleteNadeuliDeliveryImage(long nadeuliDeliveryId) throws Exception {
        log.info(nadeuliDeliveryId);
        imageRepository.deleteByNadeuliDelivery(NadeuliDelivery.builder().nadeuliDeliveryId(nadeuliDeliveryId).build());
    }


    @Override
    public void deleteImage(long imageId) throws Exception {
        log.info(imageId);
        imageRepository.deleteById(imageId);
    }
}
