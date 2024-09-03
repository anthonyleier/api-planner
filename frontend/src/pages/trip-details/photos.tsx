export function Photos() {
  return (
    <>
      <div className="flex items-center justify-between">
        <h2 className="text-3xl font-semibold">Fotos</h2>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <div className="flex items-center justify-center w-full h-auto bg-zinc-800 hover:bg-zinc-700 cursor-pointer rounded-lg">
          <span className="text-4xl text-zinc-300">+</span>
        </div>
      </div>
    </>
  );
}
