package cogitans.jpashop.service;

import cogitans.jpashop.domain.item.Book;
import cogitans.jpashop.domain.item.Item;
import cogitans.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, Book bookParam) {
        Item foundItem = itemRepository.findOne(itemId);
        foundItem.setPrice(bookParam.getPrice());
        foundItem.setName(bookParam.getName());
        foundItem.setStockQuantity(bookParam.getStockQuantity());
        return foundItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
