local result = tonumber(redis.call('BITPOS', KEYS[1], 1));
if (result > -1) then
    return result;
end;
return -1;
