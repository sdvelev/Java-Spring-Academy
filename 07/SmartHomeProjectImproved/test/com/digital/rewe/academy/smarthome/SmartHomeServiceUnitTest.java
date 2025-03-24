package com.digital.rewe.academy.smarthome;

import com.digital.rewe.academy.smarthome.api.SmartHomeServiceAPI;
import com.digital.rewe.academy.smarthome.exception.NullValueException;
import com.digital.rewe.academy.smarthome.model.Device;
import com.digital.rewe.academy.smarthome.model.SmartHome;
import com.digital.rewe.academy.smarthome.model.SmartLight;
import com.digital.rewe.academy.smarthome.validator.SmartHomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmartHomeServiceUnitTest {

    @Mock
    private SmartHomeValidator smartHomeValidatorMock;
    private SmartHomeServiceAPI smartHomeService;
    @Mock
    private SmartHome smartHomeMock;

    @BeforeEach
    void setUp() {
        smartHomeValidatorMock = Mockito.mock(SmartHomeValidator.class);
        smartHomeService = new SmartHomeService(smartHomeValidatorMock);
        smartHomeMock = Mockito.mock(SmartHome.class);
    }

    @Test
    void testAddDevicePositiveCase() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartHome(Mockito.any());
        Mockito.doNothing().when(smartHomeValidatorMock).validateDevice(Mockito.any());

        Set<Device> devicesSet = new HashSet<>();
        Mockito.when(smartHomeMock.getDevices()).thenReturn(devicesSet);

        SmartLight smartLightMock = Mockito.mock(SmartLight.class);

        smartHomeService.addDevice(smartHomeMock, smartLightMock);

        assertFalse(devicesSet.isEmpty(),
            "The collection of devices is not expected to be empty");

        assertTrue(devicesSet.contains(smartLightMock),
            "Device is expected to be present but it is not");
    }

    @Test
    void testAddDeviceWithNullDevice() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartHome(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateDevice(Mockito.any());
        assertThrows(NullValueException.class, () -> smartHomeService.addDevice(smartHomeMock, null),
            "NullValueException is expected but not thrown");
    }

    @Test
    void testRemoveDevicePositiveCase() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartHome(Mockito.any());
        Mockito.doNothing().when(smartHomeValidatorMock).validateDevice(Mockito.any());

        Set<Device> devicesSet = new HashSet<>();
        SmartLight smartLightMock = Mockito.mock(SmartLight.class);
        devicesSet.add(smartLightMock);
        Mockito.when(smartHomeMock.getDevices()).thenReturn(devicesSet);

        smartHomeService.removeDevice(smartHomeMock, smartLightMock);

        assertTrue(devicesSet.isEmpty(),
            "The collection of devices is expected to be empty after removal");
    }

    @Test
    void testRemoveDeviceWithNullDevice() {
        Mockito.doNothing().when(smartHomeValidatorMock).validateSmartHome(Mockito.any());
        Mockito.doThrow(NullValueException.class).when(smartHomeValidatorMock).validateDevice(Mockito.any());
        assertThrows(NullValueException.class, () -> smartHomeService.removeDevice(smartHomeMock, null),
            "NullValueException is expected but not thrown");
    }
}