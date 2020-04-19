import {BrowserStorageService} from './browser-storage.service';
import createSpy = jasmine.createSpy;
import SpyObj = jasmine.SpyObj;

describe('BrowserStorageService', () => {

  const KEY = 'the-key';
  const VALUE = {a: 'value'};
  const VALUE_STRING = JSON.stringify(VALUE);

  let service: BrowserStorageService;
  let mockStorage: SpyObj<Partial<Storage>>;

  beforeEach(() => {
    const store = {};

    mockStorage = {
      getItem: createSpy('getItem', (key: string): string => {
        return key in store ? store[key] : null;
      }).and.callThrough(),
      setItem: createSpy('setItem', (key: string, value: string) => {
        store[key] = `${value}`;
      }).and.callThrough(),
      removeItem: createSpy('removeItem', (key: string) => {
        delete store[key];
      }).and.callThrough()
    };

    service = new BrowserStorageService(mockStorage as Storage);
  });

  it('should write, read and remove stuff properly', () => {
    let value = service.get(KEY);
    expect(value).toBeNull();
    expect(mockStorage.getItem.calls.count()).toBe(1);
    expect(mockStorage.getItem.calls.mostRecent().args).toEqual([KEY]);

    service.set(KEY, VALUE);
    expect(mockStorage.setItem.calls.mostRecent().args).toEqual([KEY, VALUE_STRING]);

    value = service.get(KEY);
    expect(value).toEqual(VALUE);
    expect(mockStorage.getItem.calls.count()).toBe(2);

    service.remove(KEY);
    expect(mockStorage.removeItem.calls.mostRecent().args).toEqual([KEY]);

    value = service.get(KEY);
    expect(value).toBeNull();
    expect(mockStorage.getItem.calls.count()).toBe(3);
  });

  it('should gracefully deal with non-existing storage (e.g. in safari private mode)', () => {
    service = new BrowserStorageService(undefined);

    expect(() => {
      service.get(KEY);
      service.set(KEY, VALUE);
      expect(service.get(KEY)).toBeNull();
      service.remove(KEY);
    }).not.toThrow();
  });

  it('should gracefully deal with errors raised from the underlying storage', () => {
    mockStorage.getItem.and.throwError('getItem is b0rked');
    mockStorage.setItem.and.throwError('setItem is kaput');
    mockStorage.removeItem.and.throwError('remove is outta business');

    expect(() => {
      service.get(KEY);
      service.set(KEY, VALUE);
      expect(service.get(KEY)).toBeNull();
      service.remove(KEY);
    }).not.toThrow();
  });


});
