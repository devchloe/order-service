package spring.cloud.sample.clients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ItemClient {

    private ItemResource itemResource;
    private Collection<Item> itemsCache = null;

    @Autowired
    public ItemClient(ItemResource itemResource) {
        this.itemResource = itemResource;
    }

    @HystrixCommand(fallbackMethod = "getItemsCache", commandKey = "Items.findAll")
    public Collection<Item> findAll() {
        this.itemsCache = itemResource.findAll();
        return itemResource.findAll();
    }

    public Collection<Item> getItemsCache() {
        return itemsCache;
    }

    @HystrixCommand(fallbackMethod = "getOneCache")
    public Item getOne(long itemId) {
        return itemResource.getOne(itemId);
    }

    public Item getOneCache(long itemId) {
        return itemsCache.stream().filter(i -> (i.getItemId() == itemId)).findFirst().get();
    }

    @HystrixCommand(fallbackMethod = "priceCache")
    public double price(long itemId) {
        return itemResource.getOne(itemId).getPrice();
    }

    public double priceCache(long itemId) {
        return getOneCache(itemId).getPrice();
    }

}
