package kr.nadeuli.service.product.impl;

import kr.nadeuli.dto.ProductDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.entity.Member;
import kr.nadeuli.entity.Product;
import kr.nadeuli.mapper.ProductMapper;
import kr.nadeuli.scheduler.PremiumTimeScheduler;
import kr.nadeuli.service.product.ProductRepository;
import kr.nadeuli.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Transactional
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PremiumTimeScheduler premiumTimeScheduler;

    @Override
    public Long addProduct(ProductDTO productDTO) throws Exception {
        productDTO.setViewNum(0L);
        Product product = productMapper.productDTOToProduct(productDTO);
        log.info(product);
        Product savedProduct = productRepository.save(product);
        if(productDTO.isPremium()){
            premiumTimeScheduler.startPremiumTimeScheduler(savedProduct.getProductId());
        }
        log.info(savedProduct);
        return savedProduct.getProductId();
    }

    @Override
    public Long updateProduct(ProductDTO productDTO) throws Exception {
        Product beforeProduct = productRepository.findById(productDTO.getProductId()).orElse(null);
        if(beforeProduct == null){
            throw new NullPointerException("product is null");
        }
        Product product = productMapper.productDTOToProduct(productDTO);
        log.info(product);
        product.setViewNum(beforeProduct.getViewNum());

        Product savedProduct = productRepository.save(product);
        if(productDTO.isPremium()){
            premiumTimeScheduler.startPremiumTimeScheduler(savedProduct.getProductId());
        }
        log.info(savedProduct);
        return savedProduct.getProductId();
    }

    @Override
    public ProductDTO getProduct(long productId) throws Exception {
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return null;
        }
        product.setViewNum(product.getViewNum()+1);
        productRepository.save(product);
        return productMapper.productToProductDTO(product);
    }

    @Override
    public List<ProductDTO> getProductList(String gu, SearchDTO searchDTO) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize(), sort);
        Page<Product> productPage;
        if(searchDTO.getSearchKeyword() == null || searchDTO.getSearchKeyword().isEmpty()){
            productPage = productRepository.findProductList(gu, pageable);
        }else{
            productPage = productRepository.findProductListByKeyword(searchDTO.getSearchKeyword(), searchDTO.getSearchKeyword(), gu, pageable);
        }
        log.info(productPage);
        return productPage.map(productMapper::productToProductDTO).toList();
    }

    @Override
    public List<ProductDTO> getMyProductList(String tag, SearchDTO searchDTO) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize(), sort);
        Page<Product> productPage;
        log.info(searchDTO);
        log.info(tag);
        if(!searchDTO.isBuyer()){
            productPage = productRepository.findBySellerAndIsSold(Member.builder().tag(tag).build(), searchDTO.isSold(), pageable);
        }else{
            productPage = productRepository.findByBuyer(Member.builder().tag(tag).build(), pageable);
        }
        log.info(productPage);
        return productPage.map(productMapper::productToProductDTO).toList();

    }

    @Override
    public void deleteProduct(long productId) throws Exception {
        log.info(productId);
        productRepository.deleteById(productId);
    }

    @Override
    public void saleCompleted(long productId) throws Exception {
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new NullPointerException();
        }
        product.setSold(true);
        productRepository.save(product);
        log.info(product);
    }

    // false면 프리미엄이 종료, true면 프리미엄 지속
    @Override
    public boolean updatePremiumTime(long productId) throws Exception {
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new NullPointerException();
        }
        long premiumTime = product.getPremiumTime();
        if(product.getPremiumTime() == 0){
            productRepository.save(Product.builder()
                                     .productId(productId)
                                     .isPremium(false)
                                          .build());
            return false;
        }else{
            productRepository.save(Product.builder()
                                          .productId(productId)
                                          .premiumTime(premiumTime-1)
                                          .build());
            return true;
        }
    }
}
