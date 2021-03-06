package com.moneyAppV5.product.service;

import com.moneyAppV5.cart.dto.CartPositionWrapper;
import com.moneyAppV5.cart.dto.ShoppingListWrapperDTO;
import com.moneyAppV5.cart.dto.ShoppingPositionDTO;
import com.moneyAppV5.product.*;
import com.moneyAppV5.product.dto.*;
import com.moneyAppV5.product.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService
{
    private final BrandRepository brandRepository;
    private final GenreRepository genreRepository;
    private final ProductRepository repository;
    private final ShopRepository shopRepository;
    private final UnitRepository unitRepository;

    public ProductService(BrandRepository brandRepository, GenreRepository genreRepository, ProductRepository repository,
                          ShopRepository shopRepository, UnitRepository unitRepository)
    {
        this.brandRepository = brandRepository;
        this.genreRepository = genreRepository;
        this.repository = repository;
        this.shopRepository = shopRepository;
        this.unitRepository = unitRepository;
    }

    public List<GenreDTO> readAllGenresAsDto()
    {
        return this.genreRepository.findAll().stream().map(Genre::toDto).collect(Collectors.toList());
    }

    public void createGenre(GenreDTO current)
    {
        var result = current.toGenre();

        result.setHash(result.hashCode());

        this.genreRepository.save(result);
    }

    public boolean genreExistsInDatabase(String name)
    {
       return this.genreRepository.existsByName(name);
    }

    public List<UnitDTO> readAllUnitsAsDTO()
    {
        return this.unitRepository.findAll().stream().map(Unit::toDto).collect(Collectors.toList());
    }

    public List<Genre> readAllGenres()
    {
        return this.genreRepository.findAll();
    }

    public List<Unit> readAllUnits()
    {
        return this.unitRepository.findAll();
    }

    public void createProduct(ProductWriteModel current)
    {
        this.repository.save(current.toProduct());
    }

    public List<Brand> readAllBrands()
    {
        return this.brandRepository.findAll();
    }

    public List<Shop> readAllShops()
    {
        return this.shopRepository.findAll();
    }

    public void createShop(ShopDTO current)
    {
        this.shopRepository.save(current.toShop());
    }

    public List<ShopDTO> readAllShopsAsDto()
    {
       return this.shopRepository.findAll().stream().map(Shop::toDto).collect(Collectors.toList());
    }

    public void createBrand(BrandDTO current)
    {
        this.brandRepository.save(current.toBrand());
    }

    public List<BrandDTO> readAllBrandsAsDto()
    {
        return this.brandRepository.findAll().stream().map(Brand::toDto).collect(Collectors.toList());
    }

    public Map<ShopDTO, List<CartPositionWrapper>> generateCarts(ShoppingListWrapperDTO current)
    {
        var map = new HashMap<ShopDTO, List<CartPositionWrapper>>();
//        TODO
//        wrapper to lista pozycji zakupowych
//        pozycja sk??ada si?? z genre ilo??c i unit
        List<ShopDTO> shops = readAllShopsAsDto();

        for(ShoppingPositionDTO position : current.getPositions())
        {
            for (ShopDTO shop : shops)
            {
//                lista z wynikami
                var resultList = new ArrayList<CartPositionWrapper>();

//                lista produkt??w danego asortymentu z danego sklepu
                var products = this.repository.findProductsByShopNameAndGenreId(shop.getName(), position.getGenre().getId());

//                wynalezienie z ww listy najta??szej opcji aby uzyska?? wymagan?? ilo????

                var wrapper = new CartPositionWrapper();
                CartPositionWrapper temp;
//TODO czy to te?? zadzia??a dla produkt??w niepaczkowanych?
                for (Product p : products)
                {
                    temp = new CartPositionWrapper(p);
                    //                        TODO ob??uga optionala
                    temp.setPrice(this.repository.findPriceByProductIdAndShopName(p.getId(), shop.getName()).orElseThrow());

                    if  (p.getQuantity() >= position.getQuantity())
                    {
                        temp.setQuantity(position.getQuantity());
                        temp.setUnit(position.getUnit());
                    }
                    else
                    {
//                        TODO a mo??e dzielenie oczekiwanej ilo??ci przez dan?? w produkcie i wtedy wychodzi ilo???? opakowa???
                        while(temp.getQuantity() < position.getQuantity())
                        {
                            temp.setAmount(temp.getAmount() + 1);
                            temp.setQuantity(temp.getAmount() * temp.getQuantity());
                            temp.setPrice(temp.getPrice() * temp.getAmount());
                        }
                    }
                    if  (temp.getPrice() < wrapper.getPrice())
                        wrapper = temp;
                }
//                resultList.add(wrapper);

//                zapisanie w w??zku danej pozycji

                if  (map.containsKey(shop))
                    map.get(shop).add(wrapper);
                else
                {
                    resultList.add(wrapper);
                    map.put(shop, resultList);
                }
//                TODO czy przez to ??e produkt??w mo??e by?? x sztuk nale??y zamaista list<product> da?? np list<cartPos>
//                gdzie cartPos zawiera ilo???? i produkt?
            }

        }

        System.out.println();
        System.out.println(map);

        return map;
    }

    public List<ProductDTO> readAllProductsAsDto()
    {
//        TODO
        var products = this.repository.findAll();
        var result = new ArrayList<ProductDTO>();
//        product zawiera list?? cen dla danych sklep??w a dtro ma by?? ju?? z konkretnym sklepem i cen??
        for (Product p : products)
        {
            for (Price price : p.getPrices())
            {
//                tu mo??e trzeba zrobi?? toDto kt??re zapisuje pewne warto??ci i settery na cen?? i sklep?
//                albo toDto z parametrami?
                result.add(p.toDto(price.getShop().toDto(), price.getPrice()));

            }
        }

        return result;
    }
}
