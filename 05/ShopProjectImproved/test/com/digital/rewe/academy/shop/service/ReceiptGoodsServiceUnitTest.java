package com.digital.rewe.academy.shop.service;

import com.digital.rewe.academy.shop.exception.EmptyValueException;
import com.digital.rewe.academy.shop.exception.NegativeValueException;
import com.digital.rewe.academy.shop.exception.NullValueException;
import com.digital.rewe.academy.shop.model.Receipt;
import com.digital.rewe.academy.shop.validation.ReceiptValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceiptGoodsServiceUnitTest {
    @Mock
    private ReceiptValidator receiptValidatorMock;

    @Mock
    private Receipt receiptMock;

    private ReceiptGoodsService receiptGoodsService;

    @BeforeEach
    void setUp() {
        receiptMock = Mockito.mock(Receipt.class);
        receiptValidatorMock = Mockito.mock(ReceiptValidator.class);
        receiptGoodsService = new ReceiptGoodsService(receiptValidatorMock);
    }

    @Test
    void testAddGoodPositiveCase() {
        String newGoodName = "Water";
        BigDecimal newTotalPrice = BigDecimal.valueOf(1.6);
        double newQuantity = 7.;

        Mockito.doNothing().when(receiptValidatorMock).validateGoodName(newGoodName);
        Mockito.doNothing().when(receiptValidatorMock).validateTotalPrice(newTotalPrice);
        Mockito.doNothing().when(receiptValidatorMock).validateQuantity(newQuantity);

        Map<String, Map.Entry<BigDecimal, Double>> newGoodWithQuantityMap = new LinkedHashMap<>();
        Mockito.when(receiptMock.getGoodWithQuantityMap()).thenReturn(newGoodWithQuantityMap);

        receiptGoodsService.addGood(receiptMock, newGoodName, newTotalPrice, newQuantity);

        assertFalse(newGoodWithQuantityMap.isEmpty(), "Map with goods, prices and quantities is not expected to be empty");
        assertTrue(newGoodWithQuantityMap.containsKey(newGoodName), "Key with such a name is expected but is not present");
        assertEquals(newTotalPrice, newGoodWithQuantityMap.get(newGoodName).getKey(),
            "The price of goods is expected to be the same but it is not");
        assertEquals(newQuantity, newGoodWithQuantityMap.get(newGoodName).getValue(),
            "The quantity of goods is expected to be the same but it is not");
    }

    @Test
    void testAddGoodWithEmptyName() {
        String newGoodName = "";
        BigDecimal newTotalPrice = BigDecimal.valueOf(1.6);
        double newQuantity = 7.;

        Mockito.doThrow(EmptyValueException.class).when(receiptValidatorMock).validateGoodName(newGoodName);
        Mockito.doNothing().when(receiptValidatorMock).validateTotalPrice(newTotalPrice);
        Mockito.doNothing().when(receiptValidatorMock).validateQuantity(newQuantity);

        assertThrows(EmptyValueException.class, () -> receiptGoodsService.addGood(receiptMock, newGoodName,
            newTotalPrice, newQuantity), "EmptyValueException is expected but not thrown");

        Mockito.verify(receiptMock, Mockito.never()).getGoodWithQuantityMap();
    }

    @Test
    void testAddGoodWithNegativeTotalPrice() {
        String newGoodName = "Water";
        BigDecimal newTotalPrice = BigDecimal.valueOf(-1.6);
        double newQuantity = 7.;

        Mockito.doNothing().when(receiptValidatorMock).validateGoodName(newGoodName);
        Mockito.doThrow(NegativeValueException.class).when(receiptValidatorMock).validateTotalPrice(newTotalPrice);
        Mockito.doNothing().when(receiptValidatorMock).validateQuantity(newQuantity);

        assertThrows(NegativeValueException.class, () -> receiptGoodsService.addGood(receiptMock, newGoodName,
            newTotalPrice, newQuantity), "NegativeValueException is expected but not thrown");

        Mockito.verify(receiptMock, Mockito.never()).getGoodWithQuantityMap();
    }

    @Test
    void testAddGoodWithNegativeAmount() {
        String newGoodName = "Water";
        BigDecimal newTotalPrice = BigDecimal.valueOf(1.6);
        double newQuantity = -7.;

        Mockito.doNothing().when(receiptValidatorMock).validateGoodName(newGoodName);
        Mockito.doNothing().when(receiptValidatorMock).validateTotalPrice(newTotalPrice);
        Mockito.doThrow(NegativeValueException.class).when(receiptValidatorMock).validateQuantity(newQuantity);

        assertThrows(NegativeValueException.class, () -> receiptGoodsService.addGood(receiptMock, newGoodName,
            newTotalPrice, newQuantity), "NegativeValueException is expected but not thrown");

        Mockito.verify(receiptMock, Mockito.never()).getGoodWithQuantityMap();
    }

    @Test
    void testAddGoodsWithNullValue() {
        assertThrows(NullValueException.class, () -> receiptGoodsService.addGoods(receiptMock, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testRemoveGoodPositiveCase() {
        String newGoodName = "Water";
        Mockito.doNothing().when(receiptValidatorMock).validateGoodName(newGoodName);

        receiptGoodsService.removeGood(receiptMock, newGoodName);
        Mockito.verify(receiptMock, Mockito.times(1)).getGoodWithQuantityMap();
    }

    @Test
    void testRemoveGoodWithEmptyName() {
        String newGoodName = "";
        Mockito.doThrow(EmptyValueException.class).when(receiptValidatorMock).validateGoodName(newGoodName);

        assertThrows(EmptyValueException.class, () -> receiptGoodsService.removeGood(receiptMock, newGoodName),
            "EmptyValueException is expected but not thrown");

        Mockito.verify(receiptMock, Mockito.never()).getGoodWithQuantityMap();
    }
}