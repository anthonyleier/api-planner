export function Photos() {
  return (
    <>
      <div className="flex items-center justify-between">
        <h2 className="text-3xl font-semibold">Fotos</h2>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
        <img src="https://via.placeholder.com/150" className="w-full h-auto object-cover" />
        <img src="https://via.placeholder.com/150" className="w-full h-auto object-cover" />
        <img src="https://via.placeholder.com/150" className="w-full h-auto object-cover" />
        <img src="https://via.placeholder.com/150" className="w-full h-auto object-cover" />
        <img src="https://via.placeholder.com/150" className="w-full h-auto object-cover" />
        <img src="https://via.placeholder.com/150" className="w-full h-auto object-cover" />
        <img src="https://via.placeholder.com/150" className="w-full h-auto object-cover" />
        <div className="flex items-center justify-center w-full h-auto bg-gray-200 cursor-pointer">
          <span className="text-4xl text-gray-500">+</span>
        </div>
      </div>
    </>
  );
}
